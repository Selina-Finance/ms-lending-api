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

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.httpclient.adp.AdpGatewayApi;
import com.selina.lending.httpclient.adp.dto.request.Application;
import com.selina.lending.httpclient.adp.dto.request.QuickQuoteEligibilityApplicationRequest;
import com.selina.lending.httpclient.adp.dto.response.QuickQuoteEligibilityDecisionResponse;
import com.selina.lending.httpclient.adp.dto.request.Source;
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
    private QuickQuoteEligibilityDecisionResponse response;

    @Mock
    private QuickQuoteEligibilityApplicationRequest request;

    private AdpGatewayRepository adpGatewayRepository;

    @BeforeEach
    void setUp() {
        adpGatewayRepository = new AdpGatewayRepositoryImpl(api, tokenService);
    }

    @Test
    void quickQuoteEligibility() {
        //Given
        var externalApplicationId = UUID.randomUUID().toString();
        var sourceAccount = "Broker";
        var partnerAccountId = "Partner";

        when(request.getApplication()).thenReturn(application);
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

    }

    @Test
    void whenPartnerAccountIdIsNullThenDoNotSpecifyInQuickQuoteApplicationRequest() {
        //Given
        var externalApplicationId = UUID.randomUUID().toString();
        var sourceAccount = "Broker";

        when(request.getApplication()).thenReturn(application);
        when(api.quickQuoteEligibility(any())).thenReturn(response);
        when(application.getExternalApplicationId()).thenReturn(externalApplicationId);
        when(tokenService.retrieveSourceAccount()).thenReturn(sourceAccount);
        when(tokenService.retrievePartnerAccountId()).thenReturn(null);

        //When
        adpGatewayRepository.quickQuoteEligibility(request);

        //Then
        verify(application, never()).setPartnerAccountId(any());
    }
}