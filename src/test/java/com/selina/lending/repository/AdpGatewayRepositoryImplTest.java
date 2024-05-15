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


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.httpclient.adp.AdpGatewayApi;
import com.selina.lending.httpclient.adp.dto.request.Application;
import com.selina.lending.httpclient.adp.dto.request.PropertyDetails;
import com.selina.lending.httpclient.adp.dto.request.QuickQuoteEligibilityApplicationRequest;
import com.selina.lending.httpclient.adp.dto.response.Product;
import com.selina.lending.httpclient.adp.dto.response.QuickQuoteEligibilityDecisionResponse;
import com.selina.lending.httpclient.adp.dto.request.Source;
import com.selina.lending.httpclient.selection.dto.response.ProductOffer;
import com.selina.lending.service.TokenService;

@ExtendWith(MockitoExtension.class)
class AdpGatewayRepositoryImplTest {

    @Mock
    private AdpGatewayApi api;

    @Mock
    private TokenService tokenService;

    @Mock
    private Application application;

    @Mock
    private PropertyDetails propertyDetails;

    @Mock
    private Product product;

    @Mock
    private ProductOffer productOffer;

    @Mock
    private QuickQuoteEligibilityDecisionResponse response;

    @Mock
    private QuickQuoteEligibilityApplicationRequest request;

    private AdpGatewayRepository adpGatewayRepository;

    @BeforeEach
    void setUp() {
        adpGatewayRepository = new AdpGatewayRepositoryImpl(api, tokenService);
    }

    @Test
    void whenPropertyEstimatedValueNotPassedSetDefaultEstimateInRequest() {
        //Given
        var externalApplicationId = UUID.randomUUID().toString();
        var sourceAccount = "Broker";
        var partnerAccountId = "Partner";

        when(request.getApplication()).thenReturn(application);
        when(application.getPropertyDetails()).thenReturn(propertyDetails);
        when(propertyDetails.getEstimatedValue()).thenReturn(null);
        when(api.quickQuoteEligibility(any())).thenReturn(response);
        when(response.getProducts()).thenReturn(List.of(product));
        when(product.getOffer()).thenReturn(productOffer);
        when(application.getExternalApplicationId()).thenReturn(externalApplicationId);
        when(tokenService.retrieveSourceAccount()).thenReturn(sourceAccount);
        when(tokenService.retrievePartnerAccountId()).thenReturn(partnerAccountId);

        //When
        adpGatewayRepository.quickQuoteEligibility(request);

        //Then
        verify(api, times(1)).quickQuoteEligibility(request);
        verify(application, times(1)).setSource(any(Source.class));
        verify(application, times(1)).setPartnerAccountId(any(String.class));
        verify(propertyDetails, times(1)).setEstimatedValue(any(Double.class));
        verify(productOffer, times(1)).setIsAprcHeadline(any(Boolean.class));
    }

    @Test
    void whenPropertyEstimatedValuePassedDoNotSetDefaultEstimateInRequest() {
        //Given
        var externalApplicationId = UUID.randomUUID().toString();
        var sourceAccount = "Broker";
        var partnerAccountId = "Partner";

        when(request.getApplication()).thenReturn(application);
        when(application.getPropertyDetails()).thenReturn(propertyDetails);
        when(propertyDetails.getEstimatedValue()).thenReturn(50000.0);
        when(api.quickQuoteEligibility(any())).thenReturn(response);
        when(application.getExternalApplicationId()).thenReturn(externalApplicationId);
        when(tokenService.retrieveSourceAccount()).thenReturn(sourceAccount);
        when(tokenService.retrievePartnerAccountId()).thenReturn(partnerAccountId);

        //When
        adpGatewayRepository.quickQuoteEligibility(request);

        //Then
        verify(api, times(1)).quickQuoteEligibility(request);
        verify(application, times(1)).setSource(any(Source.class));
        verify(application, times(1)).setPartnerAccountId(any(String.class));
        verify(propertyDetails, never()).setEstimatedValue(any(Double.class));
        verify(productOffer,never()).setIsAprcHeadline(any(Boolean.class));


    }
    @Test
    void whenPartnerAccountIdIsNullThenDoNotSpecifyInQuickQuoteApplicationRequest() {
        //Given
        var externalApplicationId = UUID.randomUUID().toString();
        var sourceAccount = "Broker";

        when(request.getApplication()).thenReturn(application);
        when(application.getPropertyDetails()).thenReturn(propertyDetails);
        when(api.quickQuoteEligibility(any())).thenReturn(response);
        when(application.getExternalApplicationId()).thenReturn(externalApplicationId);
        when(tokenService.retrieveSourceAccount()).thenReturn(sourceAccount);
        when(tokenService.retrievePartnerAccountId()).thenReturn(null);

        //When
        adpGatewayRepository.quickQuoteEligibility(request);

        //Then
        verify(application, never()).setPartnerAccountId(any());
    }

    @ParameterizedTest
    @CsvSource({"50000.0"})
    @NullSource
    void whenProductsExistSetLTVCapToLtvBandAsDecimal(Double propertyValue) {
        //Given
        var externalApplicationId = UUID.randomUUID().toString();
        var sourceAccount = "Broker";
        var partnerAccountId = "Partner";
        var ltvBandValue = 70.0;
        var ltvBandValueAsDecimal = 0.7;

        when(request.getApplication()).thenReturn(application);
        when(application.getPropertyDetails()).thenReturn(propertyDetails);
        when(propertyDetails.getEstimatedValue()).thenReturn(propertyValue);
        when(api.quickQuoteEligibility(any())).thenReturn(response);
        when(response.getProducts()).thenReturn(List.of(product));
        when(product.getOffer()).thenReturn(productOffer);
        when(productOffer.getLtvBand()).thenReturn(ltvBandValue);
        when(application.getExternalApplicationId()).thenReturn(externalApplicationId);
        when(tokenService.retrieveSourceAccount()).thenReturn(sourceAccount);
        when(tokenService.retrievePartnerAccountId()).thenReturn(partnerAccountId);

        //When
        adpGatewayRepository.quickQuoteEligibility(request);
        verify(api, times(1)).quickQuoteEligibility(request);
        verify(application, times(1)).setSource(any(Source.class));
        verify(application, times(1)).setPartnerAccountId(any(String.class));
        verify(productOffer, times(1)).setLtvCap(ltvBandValueAsDecimal);
    }
}