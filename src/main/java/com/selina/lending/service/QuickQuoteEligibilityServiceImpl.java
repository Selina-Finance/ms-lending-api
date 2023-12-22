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

import java.util.List;

import org.springframework.stereotype.Service;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.dto.qq.request.QuickQuotePropertyDetailsDto;
import com.selina.lending.api.mapper.qq.adp.QuickQuoteEligibilityApplicationRequestMapper;
import com.selina.lending.api.mapper.qq.middleware.MiddlewareQuickQuoteApplicationRequestMapper;
import com.selina.lending.httpclient.adp.dto.request.QuickQuoteEligibilityApplicationRequest;
import com.selina.lending.httpclient.adp.dto.response.QuickQuoteEligibilityDecisionResponse;
import com.selina.lending.httpclient.eligibility.dto.response.EligibilityResponse;
import com.selina.lending.httpclient.eligibility.dto.response.PropertyInfo;
import com.selina.lending.httpclient.quickquote.Product;
import com.selina.lending.repository.AdpGatewayRepository;
import com.selina.lending.repository.EligibilityRepository;
import com.selina.lending.repository.MiddlewareRepository;
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import com.selina.lending.service.quickquote.ArrangementFeeSelinaService;
import com.selina.lending.service.quickquote.PartnerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuickQuoteEligibilityServiceImpl implements QuickQuoteEligibilityService {
    private static final String MONEVO_CLIENT_ID = "monevo";
    private static final String CLEARSCORE_CLIENT_ID = "clearscore";
    private static final double DEFAULT_ESTIMATED_VALUE = 99_999_999;

    private static final int MIN_ALLOWED_SELINA_LOAN_TERM = 5;

    private static final String ACCEPTED_DECISION = "Accepted";
    private static final String DECLINED_DECISION = "Declined";
    private static final Boolean ADD_ARRANGEMENT_FEE_SELINA_TO_LOAN_DEFAULT = false;
    private static final Boolean ADD_PRODUCT_FEES_TO_FACILITY_DEFAULT = false;

    private final MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper;

    private final AdpGatewayRepository adpGatewayRepository;
    private final MiddlewareRepository middlewareRepository;
    private final EligibilityRepository eligibilityRepository;
    private final ArrangementFeeSelinaService arrangementFeeSelinaService;
    private final PartnerService partnerService;
    private final TokenService tokenService;
    private final List<AlternativeOfferRequestProcessor> alternativeOfferRequestProcessors;

    public QuickQuoteEligibilityServiceImpl(
            MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper,
            AdpGatewayRepository adpGatewayRepository, MiddlewareRepository middlewareRepository,
            EligibilityRepository eligibilityRepository, ArrangementFeeSelinaService arrangementFeeSelinaService,
            PartnerService partnerService, TokenService tokenService,
            List<AlternativeOfferRequestProcessor> alternativeOfferRequestProcessors) {
        this.middlewareQuickQuoteApplicationRequestMapper = middlewareQuickQuoteApplicationRequestMapper;
        this.adpGatewayRepository = adpGatewayRepository;
        this.middlewareRepository = middlewareRepository;
        this.eligibilityRepository = eligibilityRepository;
        this.arrangementFeeSelinaService = arrangementFeeSelinaService;
        this.partnerService = partnerService;
        this.tokenService = tokenService;
        this.alternativeOfferRequestProcessors = alternativeOfferRequestProcessors;
    }

    @Override
    public QuickQuoteEligibilityDecisionResponse quickQuoteEligibility(QuickQuoteApplicationRequest request) {
        var clientId = tokenService.retrieveClientId();

        alternativeOfferRequestProcessors.forEach(
                processor -> processor.adjustAlternativeOfferRequest(clientId, request));

        if (hasRequestedLoanTermLessThanAllowed(request)) {
            return getDeclinedResponse();
        }

        setPropertyEstimatedValueIfDoesNotExist(request);
        setDefaultApplicantPrimaryApplicantIfDoesNotExist(request);
        QuickQuoteEligibilityApplicationRequest adpRequest = QuickQuoteEligibilityApplicationRequestMapper.mapRequest(
                request);
        enrichAdpRequestWithFees(adpRequest, clientId);

        var decisionResponse = adpGatewayRepository.quickQuoteEligibility(adpRequest);

        if (isDecisionAccepted(decisionResponse)) {
            setOffersIsAprcHeadlineToTrue(decisionResponse);
            enrichOffersWithEligibilityAndRequestWithPropertyEstimatedValue(request, decisionResponse,
                    decisionResponse.getProducts());
            storeOffersInMiddleware(request, adpRequest, decisionResponse);
        }

        return decisionResponse;
    }

    private void setPropertyEstimatedValueIfDoesNotExist(QuickQuoteApplicationRequest request) {
        if (isPropertyDetailsEstimatedValueNotSpecified(request)) {
            setDefaultPropertyDetailsEstimatedValue(request);
        }
    }

    private boolean isPropertyDetailsEstimatedValueNotSpecified(QuickQuoteApplicationRequest request) {
        return request.getPropertyDetails().getEstimatedValue() == null;
    }

    private void setDefaultPropertyDetailsEstimatedValue(QuickQuoteApplicationRequest request) {
        request.getPropertyDetails().setEstimatedValue(DEFAULT_ESTIMATED_VALUE);
    }

    private void setOffersIsAprcHeadlineToTrue(QuickQuoteEligibilityDecisionResponse response) {
        if (response.getProducts() != null) {
            response.getProducts().forEach(product -> product.getOffer().setIsAprcHeadline(true));
        }
    }

    private void enrichOffersWithEligibilityAndRequestWithPropertyEstimatedValue(QuickQuoteApplicationRequest request,
            QuickQuoteEligibilityDecisionResponse decisionResponse, List<Product> products) {
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

    private static QuickQuoteEligibilityDecisionResponse getDeclinedResponse() {
        return QuickQuoteEligibilityDecisionResponse.builder().decision(DECLINED_DECISION).build();
    }

    private void setDefaultApplicantPrimaryApplicantIfDoesNotExist(QuickQuoteApplicationRequest request) {
        if (!hasPrimaryApplicant(request.getApplicants())) {
            request.getApplicants().stream().findFirst().ifPresent(
                    quickQuoteApplicant -> quickQuoteApplicant.setPrimaryApplicant(true));
        }
    }

    private boolean hasPrimaryApplicant(List<QuickQuoteApplicantDto> quickQuoteApplicants) {
        return quickQuoteApplicants.stream().anyMatch(
                quickQuoteApplicant -> quickQuoteApplicant.getPrimaryApplicant() != null
                        && quickQuoteApplicant.getPrimaryApplicant());
    }

    private static boolean isDecisionAccepted(QuickQuoteEligibilityDecisionResponse decisionResponse) {
        return ACCEPTED_DECISION.equalsIgnoreCase(decisionResponse.getDecision())
                && decisionResponse.getProducts() != null;
    }

    private void updatePropertyEstimatedValue(QuickQuotePropertyDetailsDto propertyDetails, PropertyInfo propertyInfo) {
        if (propertyDetails.getEstimatedValue() == null) {
            Double eligibilityEstimatedValue = propertyInfo != null ? propertyInfo.getEstimatedValue() : null;
            propertyDetails.setEstimatedValue(eligibilityEstimatedValue);
            log.info(
                    "Property estimated value is not specified. Use the value from eligibility response [estimatedValue={}]",
                    eligibilityEstimatedValue);
        }
    }

    private static void enrichOffersWithEligibility(EligibilityResponse eligibilityResponse,
            QuickQuoteEligibilityDecisionResponse decisionResponse) {
        var eligibility = eligibilityResponse.getEligibility();
        decisionResponse.getProducts().forEach(product -> product.getOffer().setEligibility(eligibility));
    }

    private void enrichAdpRequestWithFees(QuickQuoteEligibilityApplicationRequest adpRequest, String clientId) {
        var tokenFees = arrangementFeeSelinaService.getFeesFromToken();

        if (adpRequest.getApplication().getFees() == null) {
            adpRequest.getApplication().setFees(tokenFees);
        }

        var requestFees = adpRequest.getApplication().getFees();

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

    private void storeOffersInMiddleware(QuickQuoteApplicationRequest request,
            QuickQuoteEligibilityApplicationRequest selectionRequest,
            QuickQuoteEligibilityDecisionResponse decisionResponse) {
        addPartner(request);
        middlewareRepository.createQuickQuoteApplication(
                middlewareQuickQuoteApplicationRequestMapper.mapToQuickQuoteRequest(request,
                        decisionResponse.getProducts(), selectionRequest.getApplication().getFees()));
    }

    private void addPartner(QuickQuoteApplicationRequest request) {
        request.setPartner(partnerService.getPartnerFromToken());
    }
}
