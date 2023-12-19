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
import com.selina.lending.api.dto.qq.request.QuickQuotePropertyDetailsDto;
import com.selina.lending.api.mapper.qq.middleware.MiddlewareQuickQuoteApplicationRequestMapper;
import com.selina.lending.api.mapper.qq.selection.QuickQuoteApplicationRequestMapper;
import com.selina.lending.exception.RemoteResourceProblemException;
import com.selina.lending.httpclient.eligibility.dto.response.EligibilityResponse;
import com.selina.lending.httpclient.eligibility.dto.response.PropertyInfo;
import com.selina.lending.httpclient.selection.dto.request.FilterQuickQuoteApplicationRequest;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.httpclient.selection.dto.response.Product;
import com.selina.lending.repository.EligibilityRepository;
import com.selina.lending.repository.MiddlewareRepository;
import com.selina.lending.repository.SelectionRepository;
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import com.selina.lending.service.quickquote.ArrangementFeeSelinaService;
import com.selina.lending.service.quickquote.PartnerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FilterApplicationServiceImpl implements FilterApplicationService {

    private static final String MONEVO_CLIENT_ID = "monevo";
    private static final String CLEARSCORE_CLIENT_ID = "clearscore";

    private static final int MIN_ALLOWED_SELINA_LOAN_TERM = 5;

    private static final String ACCEPTED_DECISION = "Accepted";
    private static final String DECLINED_DECISION = "Declined";
    private static final Boolean ADD_ARRANGEMENT_FEE_SELINA_TO_LOAN_DEFAULT = false;
    private static final Boolean ADD_PRODUCT_FEES_TO_FACILITY_DEFAULT = false;

    private final MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper;
    private final SelectionRepository selectionRepository;
    private final MiddlewareRepository middlewareRepository;
    private final EligibilityRepository eligibilityRepository;
    private final ArrangementFeeSelinaService arrangementFeeSelinaService;
    private final PartnerService partnerService;
    private final TokenService tokenService;
    private final List<AlternativeOfferRequestProcessor> alternativeOfferRequestProcessors;

    public FilterApplicationServiceImpl(MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper,
                                        SelectionRepository selectionRepository,
                                        MiddlewareRepository middlewareRepository,
                                        EligibilityRepository eligibilityRepository,
                                        ArrangementFeeSelinaService arrangementFeeSelinaService,
                                        PartnerService partnerService,
                                        TokenService tokenService,
                                        List<AlternativeOfferRequestProcessor> alternativeOfferRequestProcessors) {
        this.middlewareQuickQuoteApplicationRequestMapper = middlewareQuickQuoteApplicationRequestMapper;
        this.selectionRepository = selectionRepository;
        this.middlewareRepository = middlewareRepository;
        this.eligibilityRepository = eligibilityRepository;
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

        setDefaultApplicantPrimaryApplicantIfDoesNotExist(request);
        FilterQuickQuoteApplicationRequest selectionRequest = QuickQuoteApplicationRequestMapper.mapRequest(request);
        enrichSelectionRequestWithFees(selectionRequest, clientId);

        var decisionResponse = selectionRepository.filter(selectionRequest);

        if (isDecisionAccepted(decisionResponse)) {
            enrichOffersWithEligibilityAndRequestWithPropertyEstimatedValue(request, decisionResponse, decisionResponse.getProducts());
            storeOffersInMiddleware(request, selectionRequest, decisionResponse);
        }

        return decisionResponse;
    }

    private void enrichOffersWithEligibilityAndRequestWithPropertyEstimatedValue(QuickQuoteApplicationRequest request,
                                                                                 FilteredQuickQuoteDecisionResponse decisionResponse,
                                                                                 List<Product> products) {
        try {
            var eligibilityResponse = eligibilityRepository.getEligibility(request, products);
            updatePropertyEstimatedValue(request.getPropertyDetails(), eligibilityResponse.getPropertyInfo());
            enrichOffersWithEligibility(eligibilityResponse, decisionResponse);
        } catch (Exception ex) {
            log.error("Error retrieving eligibility. The default value from the decision service will be used.", ex);
        }
    }

    private static boolean hasRequestedLoanTermLessThanAllowed(QuickQuoteApplicationRequest request) {
        return request.getLoanInformation().getRequestedLoanTerm() < MIN_ALLOWED_SELINA_LOAN_TERM;
    }

    private static FilteredQuickQuoteDecisionResponse getDeclinedResponse() {
        return FilteredQuickQuoteDecisionResponse.builder()
                .decision(DECLINED_DECISION)
                .build();
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

    private static boolean isDecisionAccepted(FilteredQuickQuoteDecisionResponse decisionResponse) {
        return ACCEPTED_DECISION.equalsIgnoreCase(decisionResponse.getDecision()) && decisionResponse.getProducts() != null;
    }

    private void updatePropertyEstimatedValue(QuickQuotePropertyDetailsDto propertyDetails, PropertyInfo propertyInfo) {
        if (propertyDetails.getEstimatedValue() == null) {
            Double eligibilityEstimatedValue = propertyInfo != null ? propertyInfo.getEstimatedValue() : null;
            propertyDetails.setEstimatedValue(eligibilityEstimatedValue);
            log.info("Property estimated value is not specified. Use the value from eligibility response [estimatedValue={}]", eligibilityEstimatedValue);
        }
    }

    private static void enrichOffersWithEligibility(EligibilityResponse eligibilityResponse, FilteredQuickQuoteDecisionResponse decisionResponse) {
        var eligibility = eligibilityResponse.getEligibility();
        decisionResponse.getProducts().forEach(product -> product.getOffer().setEligibility(eligibility));
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

        if (isMonevoClient(clientId) || isClearScoreClient(clientId)) {
            requestFees.setIsAddArrangementFeeSelinaToLoan(true);
            requestFees.setIsAddProductFeesToFacility(true);
        }
    }

    private static boolean isClearScoreClient(String clientId) {
        return CLEARSCORE_CLIENT_ID.equalsIgnoreCase(clientId);
    }

    private static boolean isMonevoClient(String clientId) {
        return MONEVO_CLIENT_ID.equalsIgnoreCase(clientId);
    }

    private void storeOffersInMiddleware(QuickQuoteApplicationRequest request, FilterQuickQuoteApplicationRequest selectionRequest, FilteredQuickQuoteDecisionResponse decisionResponse) {
        addPartner(request);
        middlewareRepository.createQuickQuoteApplication(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(request, decisionResponse.getProducts(), selectionRequest.getApplication().getFees()));
    }

    private void addPartner(QuickQuoteApplicationRequest request) {
        request.setPartner(partnerService.getPartnerFromToken());
    }
}
