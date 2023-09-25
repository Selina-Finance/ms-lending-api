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
import com.selina.lending.httpclient.selection.dto.request.FilterQuickQuoteApplicationRequest;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
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
    private final long eligibilityReadTimeout;

    public FilterApplicationServiceImpl(MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper,
                                        SelectionRepository selectionRepository,
                                        MiddlewareRepository middlewareRepository,
                                        EligibilityRepository eligibilityRepository,
                                        ArrangementFeeSelinaService arrangementFeeSelinaService,
                                        PartnerService partnerService,
                                        TokenService tokenService,
                                        List<AlternativeOfferRequestProcessor> alternativeOfferRequestProcessors,
                                        @Value("${service.ms-eligibility.readTimeout}") long eligibilityReadTimeout) {
        this.middlewareQuickQuoteApplicationRequestMapper = middlewareQuickQuoteApplicationRequestMapper;
        this.selectionRepository = selectionRepository;
        this.middlewareRepository = middlewareRepository;
        this.eligibilityRepository = eligibilityRepository;
        this.arrangementFeeSelinaService = arrangementFeeSelinaService;
        this.partnerService = partnerService;
        this.tokenService = tokenService;
        this.alternativeOfferRequestProcessors = alternativeOfferRequestProcessors;
        this.eligibilityReadTimeout = eligibilityReadTimeout;
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
        var decisionResponse = getOffers(selectionRequest, request.getPropertyDetails());

        if (isDecisionAccepted(decisionResponse)) {
            storeOffersInMiddleware(request, selectionRequest, decisionResponse);
        }

        return decisionResponse;
    }

    private static boolean isDecisionAccepted(FilteredQuickQuoteDecisionResponse decisionResponse) {
        return ACCEPTED_DECISION.equalsIgnoreCase(decisionResponse.getDecision()) && decisionResponse.getProducts() != null;
    }

    @SneakyThrows
    private FilteredQuickQuoteDecisionResponse getOffers(FilterQuickQuoteApplicationRequest selectionRequest, QuickQuotePropertyDetailsDto propertyDetails) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var decisionResponseFuture = getDecisionResponseAsync(selectionRequest, authentication);
        var eligibilityResponseFuture = getEligibilityAsync(propertyDetails, authentication);

        try {
            var decisionResponse = decisionResponseFuture.get();

            if (isDecisionAccepted(decisionResponse)) {
                enrichOffersWithEligibility(eligibilityResponseFuture.get(), decisionResponse);
            }

            return decisionResponse;
        } catch (InterruptedException | ExecutionException ex) {
            log.error("An error occurred while getting offers", ex);

            if (!decisionResponseFuture.isCompletedExceptionally()) {
                log.warn("Return offers with default eligibility");
                return decisionResponseFuture.get();
            }

            throw new RemoteResourceProblemException();
        }
    }

    private CompletableFuture<FilteredQuickQuoteDecisionResponse> getDecisionResponseAsync(FilterQuickQuoteApplicationRequest selectionRequest, Authentication authentication) {
        return CompletableFuture.supplyAsync(() -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return selectionRepository.filter(selectionRequest);
        }).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Error getting decision", ex);
            } else {
                log.info("Successfully got [decision={}]", result.getDecision());
            }
        });
    }

    private CompletableFuture<EligibilityResponse> getEligibilityAsync(QuickQuotePropertyDetailsDto propertyDetails, Authentication authentication) {
        return CompletableFuture.supplyAsync(() -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return eligibilityRepository.getEligibility(propertyDetails);
        }).orTimeout(eligibilityReadTimeout, TimeUnit.MILLISECONDS)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error getting eligibility", ex);
                    } else {
                        log.info("Successfully got [eligibility={}]", result.getEligibility());
                    }
                });
    }

    private static void enrichOffersWithEligibility(EligibilityResponse eligibilityResponse, FilteredQuickQuoteDecisionResponse decisionResponse) throws InterruptedException, ExecutionException {
        var eligibility = eligibilityResponse.getEligibility();
        decisionResponse.getProducts().forEach(product -> product.getOffer().setEligibility(eligibility));
    }

    private static boolean hasRequestedLoanTermLessThanAllowed(QuickQuoteApplicationRequest request) {
        return request.getLoanInformation().getRequestedLoanTerm() < MIN_ALLOWED_SELINA_LOAN_TERM;
    }

    private static FilteredQuickQuoteDecisionResponse getDeclinedResponse() {
        return FilteredQuickQuoteDecisionResponse.builder()
                .decision(DECLINED_DECISION)
                .build();
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

    private void storeOffersInMiddleware(QuickQuoteApplicationRequest request, FilterQuickQuoteApplicationRequest selectionRequest, FilteredQuickQuoteDecisionResponse decisionResponse) {
        setDefaultApplicantPrimaryApplicantIfDoesNotExist(request);
        addPartner(request);
        middlewareRepository.createQuickQuoteApplication(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(request, decisionResponse.getProducts(), selectionRequest.getApplication().getFees()));
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
