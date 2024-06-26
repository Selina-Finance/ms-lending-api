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

package com.selina.lending.repository;

import org.springframework.stereotype.Service;

import com.selina.lending.httpclient.adp.AdpGatewayApi;
import com.selina.lending.httpclient.adp.dto.request.QuickQuoteEligibilityApplicationRequest;
import com.selina.lending.httpclient.adp.dto.response.QuickQuoteEligibilityDecisionResponse;
import com.selina.lending.httpclient.adp.dto.request.Source;
import com.selina.lending.httpclient.adp.dto.request.SourceAccount;
import com.selina.lending.service.LendingConstants;
import com.selina.lending.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdpGatewayRepositoryImpl implements AdpGatewayRepository {

    private static final double DEFAULT_ESTIMATED_VALUE = 9999999;
    private final AdpGatewayApi api;
    private final TokenService tokenService;

    public AdpGatewayRepositoryImpl(AdpGatewayApi api, TokenService tokenService) {
        this.api = api;
        this.tokenService = tokenService;
    }

    @Override
    public QuickQuoteEligibilityDecisionResponse quickQuoteEligibility(QuickQuoteEligibilityApplicationRequest request) {
        log.info("Quick Quote Eligibility application [externalApplicationId={}]",
                request.getApplication().getExternalApplicationId());
        enrichRequest(request);
        if (isPropertyDetailsEstimatedValueSpecified(request)) {
            var response = api.quickQuoteEligibility(request);
            setLtvCapToLtvBandAsDecimal(response);
            return response;
        }
        setDefaultPropertyDetailsEstimatedValue(request);
        var response = api.quickQuoteEligibility(request);
        setOffersIsAprcHeadlineToTrue(response);
        setLtvCapToLtvBandAsDecimal(response);
        return response;
    }


    private void setLtvCapToLtvBandAsDecimal(QuickQuoteEligibilityDecisionResponse response) {
        if (response.getProducts() != null) {
            response.getProducts().forEach(product -> product.getOffer().setLtvCap(convertToDecimal(product.getOffer().getLtvBand())));
        }
    }

    private Double convertToDecimal(Double ltvBand) {
        return ltvBand / 100;
    }

    private void enrichRequest(QuickQuoteEligibilityApplicationRequest request) {
        var application = request.getApplication();
        var source = Source.builder().name(LendingConstants.REQUEST_SOURCE).account(
                SourceAccount.builder().name(tokenService.retrieveSourceAccount()).build()).build();
        application.setSource(source);

        var partnerAccountId = tokenService.retrievePartnerAccountId();
        if (partnerAccountId != null) {
            application.setPartnerAccountId(partnerAccountId);
        }
    }

    private void setOffersIsAprcHeadlineToTrue(QuickQuoteEligibilityDecisionResponse response) {
        if (response.getProducts() != null) {
            response.getProducts().forEach(product -> product.getOffer().setIsAprcHeadline(true));
        }
    }

    private void setDefaultPropertyDetailsEstimatedValue(QuickQuoteEligibilityApplicationRequest request) {
        request.getApplication().getPropertyDetails().setEstimatedValue(DEFAULT_ESTIMATED_VALUE);
    }

    private boolean isPropertyDetailsEstimatedValueSpecified(QuickQuoteEligibilityApplicationRequest request) {
        return request.getApplication().getPropertyDetails() != null && request.getApplication().getPropertyDetails().getEstimatedValue() != null;
    }
}
