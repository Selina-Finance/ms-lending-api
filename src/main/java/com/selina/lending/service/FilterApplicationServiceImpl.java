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

import static com.selina.lending.service.LendingConstants.ACCEPT_DECISION;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.dto.qq.request.QuickQuotePropertyDetailsDto;
import com.selina.lending.api.dto.qq.response.QuickQuoteResponse;
import com.selina.lending.api.mapper.qq.adp.QuickQuoteEligibilityApplicationRequestMapper;
import com.selina.lending.api.mapper.qq.adp.QuickQuoteEligibilityApplicationResponseMapper;
import com.selina.lending.api.mapper.qq.middleware.MiddlewareQuickQuoteApplicationRequestMapper;
import com.selina.lending.api.mapper.qq.selection.QuickQuoteApplicationRequestMapper;
import com.selina.lending.api.mapper.qq.selection.QuickQuoteApplicationResponseMapper;
import com.selina.lending.httpclient.adp.dto.request.QuickQuoteEligibilityApplicationRequest;
import com.selina.lending.httpclient.adp.dto.response.Product;
import com.selina.lending.httpclient.adp.dto.response.QuickQuoteEligibilityDecisionResponse;
import com.selina.lending.httpclient.eligibility.dto.response.PropertyInfo;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.selection.dto.request.FilterQuickQuoteApplicationRequest;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.repository.AdpGatewayRepository;
import com.selina.lending.repository.EligibilityRepository;
import com.selina.lending.repository.MiddlewareRepository;
import com.selina.lending.repository.SelectionRepository;
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import com.selina.lending.service.quickquote.ArrangementFeeSelinaService;
import com.selina.lending.service.quickquote.PartnerService;
import com.selina.lending.util.ABTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilterApplicationServiceImpl implements FilterApplicationService {

    protected static final String ADP_CLIENT_ID = "the-aggregator-adp";
    private static final String MONEVO_CLIENT_ID = "monevo";
    private static final String CLEARSCORE_CLIENT_ID = "clearscore";
    private static final String EXPERIAN_CLIENT_ID = "experian";

    private static final LeadDto GO_COMPARE_PARTNER_UTM = LeadDto.builder()
            .utmSource("aggregator")
            .utmMedium("cpc")
            .utmCampaign("_consumer_referral___gocompare_main_")
            .build();

    protected static final String MS_QUICK_QUOTE_CLIENT_ID = "ms-quick-quote";

    private static final int MIN_ALLOWED_SELINA_LOAN_TERM = 5;

    private static final String ACCEPTED_DECISION = "Accepted";
    private static final String DECLINED_DECISION = "Declined";

    private static final Boolean ADD_ARRANGEMENT_FEE_SELINA_TO_LOAN_DEFAULT = false;
    private static final Boolean ADD_PRODUCT_FEES_TO_FACILITY_DEFAULT = false;

    private static final String HELOC_PRODUCT_FAMILY = "HELOC";
    private static final String HOMEOWNER_LOAN_PRODUCT_FAMILY = "Homeowner Loan";

    private static final Double ELIGIBILITY_100 = 100.0;
    private static final Double ELIGIBILITY_95 = 95.0;

    private static final String TEST_GROUP_ID_GRO_2936_FORMAT = "GRO-2936: %s";
    private static final String TEST_GROUP_ID_GRO_2888_FORMAT = "GRO-2888: %s";
    private static final String GROUP_A = "Group A";
    private static final String GROUP_B = "Group B";

    private final MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper;
    private final SelectionRepository selectionRepository;
    private final MiddlewareRepository middlewareRepository;
    private final EligibilityRepository eligibilityRepository;

    private final AdpGatewayRepository adpGatewayRepository;

    private final ArrangementFeeSelinaService arrangementFeeSelinaService;
    private final PartnerService partnerService;
    private final TokenService tokenService;
    private final List<AlternativeOfferRequestProcessor> alternativeOfferRequestProcessors;

    private final boolean isFilterClearScoreResponseOffersFeatureEnabled;
    private final boolean isFilterExperianResponseOffersFeatureEnabled;

    private final boolean isAdpGatewayClientMsQuickQuoteEnabled;

    public FilterApplicationServiceImpl(MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper,
            SelectionRepository selectionRepository,
            MiddlewareRepository middlewareRepository,
            EligibilityRepository eligibilityRepository,
            AdpGatewayRepository adpGatewayRepository,
            ArrangementFeeSelinaService arrangementFeeSelinaService,
            PartnerService partnerService,
            TokenService tokenService,
            List<AlternativeOfferRequestProcessor> alternativeOfferRequestProcessors,
            @Value("${features.filterResponseOffers.clearscore.enabled}")
            boolean isFilterClearScoreResponseOffersFeatureEnabled,
            @Value("${features.filterResponseOffers.experian.enabled}")
            boolean isFilterExperianResponseOffersFeatureEnabled,
            @Value("${features.adp-gateway.clients.ms-quick-quote.enabled}")
            boolean isAdpGatewayClientMsQuickQuoteEnabled) {
        this.middlewareQuickQuoteApplicationRequestMapper = middlewareQuickQuoteApplicationRequestMapper;
        this.selectionRepository = selectionRepository;
        this.middlewareRepository = middlewareRepository;
        this.eligibilityRepository = eligibilityRepository;
        this.adpGatewayRepository = adpGatewayRepository;
        this.arrangementFeeSelinaService = arrangementFeeSelinaService;
        this.partnerService = partnerService;
        this.tokenService = tokenService;
        this.alternativeOfferRequestProcessors = alternativeOfferRequestProcessors;
        this.isFilterClearScoreResponseOffersFeatureEnabled = isFilterClearScoreResponseOffersFeatureEnabled;
        this.isFilterExperianResponseOffersFeatureEnabled = isFilterExperianResponseOffersFeatureEnabled;
        this.isAdpGatewayClientMsQuickQuoteEnabled = isAdpGatewayClientMsQuickQuoteEnabled;
    }

    @Override
    public QuickQuoteResponse filter(QuickQuoteApplicationRequest request) {
        QuickQuoteResponse quickQuoteResponse;
        var clientId = tokenService.retrieveClientId();

        setDefaultPrimaryApplicantIfDoesNotExist(request);
        storeOriginalRequestedLoanTerm(request);
        alternativeOfferRequestProcessors.forEach(processor -> processor.adjustAlternativeOfferRequest(clientId, request));

        if (hasRequestedLoanTermLessThanAllowed(request)) {
            return getDeclinedResponse();
        }

        quickQuoteResponse = getQuickQuoteResponse(request, clientId);

        return quickQuoteResponse;
    }

    private void storeOriginalRequestedLoanTerm(QuickQuoteApplicationRequest request) {
        request.getLoanInformation().setOriginalRequestedLoanTerm(request.getLoanInformation().getRequestedLoanTerm());
    }

    private QuickQuoteResponse getQuickQuoteResponse(QuickQuoteApplicationRequest request, String clientId) {
        QuickQuoteResponse quickQuoteResponse;
        if (isAdpClient(clientId) || (isAdpGatewayClientMsQuickQuoteEnabled && isMsQuickQuoteClient(clientId))) {
            log.info("Use ADP decisioning engine");
            QuickQuoteEligibilityApplicationRequest adpRequest = QuickQuoteEligibilityApplicationRequestMapper.mapRequest(request);
            enrichAdpRequestWithFees(adpRequest, clientId);
            var adpResponse  = adpGatewayRepository.quickQuoteEligibility(adpRequest);

            if (isDecisionAccepted(adpResponse.getDecision(), adpResponse.getProducts())) {
                adpResponse.setProducts(getFilteredResponseOffers(clientId, request, adpResponse.getProducts()));
                enrichOffersWithEligibilityAndRequestWithPropertyEstimatedValue(request, adpResponse.getProducts(), adpResponse.getHasReferOffers());
                storeOffersInMiddleware(request, adpRequest.getApplication().getFees(), adpResponse.getProducts());
                filterOffersByAcceptDecision(adpResponse);
                quickQuoteResponse = QuickQuoteEligibilityApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(adpResponse);
            } else {
                quickQuoteResponse = getDeclinedResponse();
            }
        } else {
            FilterQuickQuoteApplicationRequest selectionRequest = QuickQuoteApplicationRequestMapper.mapRequest(request);
            enrichSelectionRequestWithFees(selectionRequest, clientId);
            FilteredQuickQuoteDecisionResponse filteredQuickQuoteDecisionResponse = selectionRepository.filter(selectionRequest);

            if (isDecisionAccepted(filteredQuickQuoteDecisionResponse.getDecision(), filteredQuickQuoteDecisionResponse.getProducts())) {
                filteredQuickQuoteDecisionResponse.setProducts(getFilteredResponseOffers(clientId, request, filteredQuickQuoteDecisionResponse.getProducts()));
                enrichOffersWithEligibilityAndRequestWithPropertyEstimatedValue(request, filteredQuickQuoteDecisionResponse.getProducts(),
                        filteredQuickQuoteDecisionResponse.getHasReferOffers());
                storeOffersInMiddleware(request, selectionRequest.getApplication().getFees(), filteredQuickQuoteDecisionResponse.getProducts());
            }
            quickQuoteResponse = QuickQuoteApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(filteredQuickQuoteDecisionResponse);
        }

        return quickQuoteResponse;
    }

    private List<Product> getFilteredResponseOffers(String clientId, QuickQuoteApplicationRequest request, List<Product> productList) {
        if (isFilterClearScoreResponseOffersFeatureEnabled && isClearScoreClient(clientId)) {
            var hasOddPrimaryApplicantBirthday = ABTestUtils.hasOddPrimaryApplicantBirthday(request.getApplicants());
            ABTestUtils.appendTestGroupId(request, TEST_GROUP_ID_GRO_2888_FORMAT.formatted(hasOddPrimaryApplicantBirthday ? GROUP_B : GROUP_A));

            var filteredProducts = new ArrayList<Product>(2);
            findTheLowestAprcProduct(productList, HELOC_PRODUCT_FAMILY).ifPresent(filteredProducts::add);
            findTheLowestAprcProduct(productList, HOMEOWNER_LOAN_PRODUCT_FAMILY, hasOddPrimaryApplicantBirthday).ifPresent(filteredProducts::add);
            return filteredProducts;
        }

        if (isFilterExperianResponseOffersFeatureEnabled && isExperianClient(clientId) && isGoComparePartner(request.getLead())) {
            var filteredProducts = new ArrayList<Product>(2);
            findTheLowestAprcProduct(productList, HOMEOWNER_LOAN_PRODUCT_FAMILY).ifPresent(filteredProducts::add);
            return filteredProducts;
        }

        return productList;
    }

    private Optional<Product> findTheLowestAprcProduct(List<Product> products, String family, Boolean isVariable) {
        return products.stream()
                .filter(product -> family.equalsIgnoreCase(product.getFamily()) && isVariable.equals(product.getIsVariable()))
                .min(Comparator.comparingDouble(product -> product.getOffer().getAprc()));
    }

    private Optional<Product> findTheLowestAprcProduct(List<Product> products, String family) {
        return products.stream()
                .filter(product -> family.equalsIgnoreCase(product.getFamily()))
                .min(Comparator.comparingDouble(product -> product.getOffer().getAprc()));
    }

    private void enrichOffersWithEligibilityAndRequestWithPropertyEstimatedValue(QuickQuoteApplicationRequest request, List<Product> products, Boolean hasReferOffers) {
        try {
            var eligibilityResponse = eligibilityRepository.getEligibility(request, products, hasReferOffers);
            updatePropertyEstimatedValue(request.getPropertyDetails(), eligibilityResponse.getPropertyInfo());

            var eligibility = eligibilityResponse.getEligibility();
            if (ELIGIBILITY_100.equals(eligibility)) {
                if (ABTestUtils.hasOddPrimaryApplicantBirthday(request.getApplicants())) {
                    eligibility = ELIGIBILITY_95;
                    ABTestUtils.appendTestGroupId(request, TEST_GROUP_ID_GRO_2936_FORMAT.formatted(GROUP_B));
                } else {
                    ABTestUtils.appendTestGroupId(request, TEST_GROUP_ID_GRO_2936_FORMAT.formatted(GROUP_A));
                }
            }
            enrichOffersWithEligibility(eligibility, products);
        } catch (Exception ex) {
            log.error("Error retrieving eligibility. The default value from the decision service will be used.", ex);
        }
    }

    private static boolean hasRequestedLoanTermLessThanAllowed(QuickQuoteApplicationRequest request) {
        return request.getLoanInformation().getRequestedLoanTerm() < MIN_ALLOWED_SELINA_LOAN_TERM;
    }

    private static QuickQuoteResponse getDeclinedResponse() {
        return QuickQuoteResponse.builder()
                .status(DECLINED_DECISION)
                .build();
    }

    private void setDefaultPrimaryApplicantIfDoesNotExist(QuickQuoteApplicationRequest request) {
        if (!hasPrimaryApplicant(request.getApplicants())) {
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

    private static boolean isDecisionAccepted(String decision, List<Product> products) {
        return ACCEPTED_DECISION.equalsIgnoreCase(decision) && products != null;
    }

    private void updatePropertyEstimatedValue(QuickQuotePropertyDetailsDto propertyDetails, PropertyInfo propertyInfo) {
        if (propertyDetails.getEstimatedValue() == null) {
            Double eligibilityEstimatedValue = propertyInfo != null ? propertyInfo.getEstimatedValue() : null;
            propertyDetails.setEstimatedValue(eligibilityEstimatedValue);
            log.info("Property estimated value is not specified. Use the value from eligibility response [estimatedValue={}]", eligibilityEstimatedValue);
        }
    }

    private static void enrichOffersWithEligibility(Double eligibility, List<Product>products) {
        products.forEach(product -> product.getOffer().setEligibility(eligibility));
    }

    private void enrichAdpRequestWithFees(QuickQuoteEligibilityApplicationRequest adpRequest, String clientId) {
        var tokenFees = arrangementFeeSelinaService.getFeesFromToken();

        if (adpRequest.getApplication().getFees() == null) {
            adpRequest.getApplication().setFees(tokenFees);
        }
        enrichRequestWithFees(adpRequest.getApplication().getFees(), clientId, tokenFees);
    }

    private void enrichSelectionRequestWithFees(FilterQuickQuoteApplicationRequest selectionRequest, String clientId) {
        var tokenFees = arrangementFeeSelinaService.getFeesFromToken();

        if (selectionRequest.getApplication().getFees() == null) {
            selectionRequest.getApplication().setFees(tokenFees);
        }
        enrichRequestWithFees(selectionRequest.getApplication().getFees(), clientId, tokenFees);
    }

    private void enrichRequestWithFees(Fees requestFees, String clientId, Fees tokenFees) {
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

    private static boolean isAdpClient(String clientId) {
        return ADP_CLIENT_ID.equalsIgnoreCase(clientId);
    }

    private static boolean isClearScoreClient(String clientId) {
        return CLEARSCORE_CLIENT_ID.equalsIgnoreCase(clientId);
    }

    private static boolean isMonevoClient(String clientId) {
        return MONEVO_CLIENT_ID.equalsIgnoreCase(clientId);
    }

    private static boolean isExperianClient(String clientId) {
        return EXPERIAN_CLIENT_ID.equalsIgnoreCase(clientId);
    }

    private boolean isGoComparePartner(LeadDto leadDto) {
        return GO_COMPARE_PARTNER_UTM.equals(leadDto);
    }

    private static boolean isMsQuickQuoteClient(String clientId) {
        return MS_QUICK_QUOTE_CLIENT_ID.equalsIgnoreCase(clientId);
    }

    private void storeOffersInMiddleware(QuickQuoteApplicationRequest request, Fees fees, List<Product> products) {
        addPartner(request);
        middlewareRepository.createQuickQuoteApplication(middlewareQuickQuoteApplicationRequestMapper.mapToQuickQuoteRequest(request, products, fees));
    }

    private void addPartner(QuickQuoteApplicationRequest request) {
        request.setPartner(partnerService.getPartnerFromToken());
    }

    public void filterOffersByAcceptDecision(QuickQuoteEligibilityDecisionResponse response) {
        List<Product> products = response.getProducts();
        response.setProducts(products.stream()
                .filter(product -> ACCEPT_DECISION.equalsIgnoreCase(product.getOffer().getDecision()))
                .toList());
    }
}