/*
 * Copyright 2022 Selina Finance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.selina.lending.service;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.mapper.qq.middleware.MiddlewareQuickQuoteApplicationRequestMapper;
import com.selina.lending.api.mapper.qq.selection.QuickQuoteApplicationRequestMapper;
import com.selina.lending.httpclient.selection.dto.request.FilterQuickQuoteApplicationRequest;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.repository.MiddlewareRepository;
import com.selina.lending.repository.SelectionRepository;
import com.selina.lending.service.quickquote.ArrangementFeeSelinaService;
import com.selina.lending.service.quickquote.PartnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FilterApplicationServiceImpl implements FilterApplicationService {

    private static final String CLEAR_SCORE_CLIENT_ID = "clearscore";
    private static final String MONEVO_CLIENT_ID = "monevo";
    private static final int MIN_ALLOWED_SELINA_LOAN_TERM = 5;
    private static final int MIN_ALLOWED_CLEAR_SCORE_ALTERNATIVE_OFFER_LOAN_TERM = 3;

    private static final String ACCEPTED_DECISION = "Accepted";
    private static final String DECLINED_DECISION = "Declined";
    private static final Boolean ADD_ARRANGEMENT_FEE_SELINA_TO_LOAN_DEFAULT = false;
    private static final Boolean ADD_PRODUCT_FEES_TO_FACILITY_DEFAULT = false;

    private final MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper;
    private final SelectionRepository selectionRepository;
    private final MiddlewareRepository middlewareRepository;
    private final ArrangementFeeSelinaService arrangementFeeSelinaService;
    private final PartnerService partnerService;
    private final TokenService tokenService;

    public FilterApplicationServiceImpl(MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper,
                                        SelectionRepository selectionRepository,
                                        MiddlewareRepository middlewareRepository,
                                        ArrangementFeeSelinaService arrangementFeeSelinaService,
                                        PartnerService partnerService,
                                        TokenService tokenService) {
        this.middlewareQuickQuoteApplicationRequestMapper = middlewareQuickQuoteApplicationRequestMapper;
        this.selectionRepository = selectionRepository;
        this.middlewareRepository = middlewareRepository;
        this.arrangementFeeSelinaService = arrangementFeeSelinaService;
        this.partnerService = partnerService;
        this.tokenService = tokenService;
    }

    @Override
    public FilteredQuickQuoteDecisionResponse filter(QuickQuoteApplicationRequest request) {
        var clientId = tokenService.retrieveClientId();
        var requestedLoanTerm = request.getLoanInformation().getRequestedLoanTerm();

        if (isAlternativeOfferRequest(requestedLoanTerm)) {
            if (isAllowedAlternativeOfferRequest(clientId, requestedLoanTerm)) {
                adjustToAlternativeOffer(request);
            } else {
                return getDeclinedResponse();
            }
        }

        FilterQuickQuoteApplicationRequest selectionRequest = QuickQuoteApplicationRequestMapper.mapRequest(request);
        enrichSelectionRequestWithFees(selectionRequest, clientId);
        FilteredQuickQuoteDecisionResponse decisionResponse = selectionRepository.filter(selectionRequest);

        if (ACCEPTED_DECISION.equalsIgnoreCase(decisionResponse.getDecision()) && decisionResponse.getProducts() != null) {
            setDefaultApplicantPrimaryApplicantIfDoesNotExist(request);
            addPartner(request);
            middlewareRepository.createQuickQuoteApplication(middlewareQuickQuoteApplicationRequestMapper
                    .mapToQuickQuoteRequest(request, decisionResponse.getProducts(), selectionRequest.getApplication().getFees()));
        }

        return decisionResponse;
    }

    private static FilteredQuickQuoteDecisionResponse getDeclinedResponse() {
        return FilteredQuickQuoteDecisionResponse.builder()
                .decision(DECLINED_DECISION)
                .build();
    }

    private static boolean isAllowedAlternativeOfferRequest(String clientId, Integer requestedLoanTerm) {
        return isAlternativeOfferRequest(requestedLoanTerm)
                && (isClearScoreAlternativeOfferRequest(clientId, requestedLoanTerm) || isMonevoAlternativeOfferRequest(clientId, requestedLoanTerm));
    }

    private static boolean isAlternativeOfferRequest(Integer requestedLoanTerm) {
        return requestedLoanTerm < MIN_ALLOWED_SELINA_LOAN_TERM;
    }

    private static boolean isClearScoreAlternativeOfferRequest(String clientId, Integer requestedLoanTerm) {
        return CLEAR_SCORE_CLIENT_ID.equalsIgnoreCase(clientId)
                && requestedLoanTerm >= MIN_ALLOWED_CLEAR_SCORE_ALTERNATIVE_OFFER_LOAN_TERM
                && requestedLoanTerm < MIN_ALLOWED_SELINA_LOAN_TERM;
    }

    private static boolean isMonevoAlternativeOfferRequest(String clientId, Integer requestedLoanTerm) {
        return MONEVO_CLIENT_ID.equalsIgnoreCase(clientId) && requestedLoanTerm < MIN_ALLOWED_SELINA_LOAN_TERM;
    }

    private void adjustToAlternativeOffer(QuickQuoteApplicationRequest request) {
        try {
            var clientId = tokenService.retrieveClientId();
            var requestedLoanTerm = request.getLoanInformation().getRequestedLoanTerm();

            if (isMonevoAlternativeOfferRequest(clientId, requestedLoanTerm) || isClearScoreAlternativeOfferRequest(clientId, requestedLoanTerm)) {
                request.getLoanInformation().setRequestedLoanTerm(MIN_ALLOWED_SELINA_LOAN_TERM);
                log.info("Adjust QQ application to alternative offer [clientId={}] [externalApplicationId={}] [originalRequestedLoanTerm={}]",
                        clientId, request.getExternalApplicationId(), requestedLoanTerm);
            }
        } catch (Exception ex) {
            log.error("An error occurred while adjusting to alternative offer [externalApplicationId={}]", request.getExternalApplicationId(), ex);
        }
    }

    private void enrichSelectionRequestWithFees(FilterQuickQuoteApplicationRequest selectionRequest, String clientId) {
        var tokenFees = arrangementFeeSelinaService.getFeesFromToken();

        if (selectionRequest.getApplication().getFees() == null) {
            selectionRequest.getApplication().setFees(tokenFees);
        }

        var requestFees = selectionRequest.getApplication().getFees();

        requestFees.setAddArrangementFeeSelina(tokenFees.getAddArrangementFeeSelina());
        requestFees.setArrangementFeeDiscountSelina(tokenFees.getArrangementFeeDiscountSelina());

        if (requestFees.getIsAddArrangementFeeSelinaToLoan() == null) {
            requestFees.setIsAddArrangementFeeSelinaToLoan(ADD_ARRANGEMENT_FEE_SELINA_TO_LOAN_DEFAULT);
        }

        if (requestFees.getIsAddProductFeesToFacility() == null) {
            requestFees.setIsAddProductFeesToFacility(ADD_PRODUCT_FEES_TO_FACILITY_DEFAULT);
        }

        if (MONEVO_CLIENT_ID.equalsIgnoreCase(clientId)) {
            requestFees.setIsAddArrangementFeeSelinaToLoan(true);
            requestFees.setIsAddProductFeesToFacility(true);
        }
    }

    private void setDefaultApplicantPrimaryApplicantIfDoesNotExist(QuickQuoteApplicationRequest request) {
        if(!hasPrimaryApplicant(request.getApplicants())) {
            request.getApplicants().stream().findFirst()
                    .ifPresent(quickQuoteApplicant -> quickQuoteApplicant.setPrimaryApplicant(true));
        }
    }

    private boolean hasPrimaryApplicant(List<QuickQuoteApplicantDto> quickQuoteApplicants) {
        return quickQuoteApplicants
                .stream()
                .anyMatch(quickQuoteApplicant -> quickQuoteApplicant.getPrimaryApplicant() != null
                        && quickQuoteApplicant.getPrimaryApplicant());
    }

    private void addPartner(QuickQuoteApplicationRequest request) {
        request.setPartner(partnerService.getPartnerFromToken());
    }
}
