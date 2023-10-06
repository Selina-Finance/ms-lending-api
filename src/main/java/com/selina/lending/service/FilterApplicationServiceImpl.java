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
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import com.selina.lending.service.quickquote.ArrangementFeeSelinaService;
import com.selina.lending.service.quickquote.PartnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class FilterApplicationServiceImpl implements FilterApplicationService {

    private static final String MONEVO_CLIENT_ID = "monevo";

    private static final int MIN_ALLOWED_SELINA_LOAN_TERM = 5;

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
    private final List<AlternativeOfferRequestProcessor> alternativeOfferRequestProcessors;

    public FilterApplicationServiceImpl(MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper,
                                        SelectionRepository selectionRepository,
                                        MiddlewareRepository middlewareRepository,
                                        ArrangementFeeSelinaService arrangementFeeSelinaService,
                                        PartnerService partnerService,
                                        TokenService tokenService,
                                        List<AlternativeOfferRequestProcessor> alternativeOfferRequestProcessors) {
        this.middlewareQuickQuoteApplicationRequestMapper = middlewareQuickQuoteApplicationRequestMapper;
        this.selectionRepository = selectionRepository;
        this.middlewareRepository = middlewareRepository;
        this.arrangementFeeSelinaService = arrangementFeeSelinaService;
        this.partnerService = partnerService;
        this.tokenService = tokenService;
        this.alternativeOfferRequestProcessors = alternativeOfferRequestProcessors;
    }

    @Override
    public FilteredQuickQuoteDecisionResponse filter(QuickQuoteApplicationRequest request) {
        var clientId = tokenService.retrieveClientId();

        alternativeOfferRequestProcessors.forEach(processor -> processor.adjustAlternativeOfferRequest(clientId, request));

        if (hasRequestedLoanTermLessThanAllowed(request)) {
            return getDeclinedResponse();
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

    private static boolean hasRequestedLoanTermLessThanAllowed(QuickQuoteApplicationRequest request) {
        return request.getLoanInformation().getRequestedLoanTerm() < MIN_ALLOWED_SELINA_LOAN_TERM;
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
