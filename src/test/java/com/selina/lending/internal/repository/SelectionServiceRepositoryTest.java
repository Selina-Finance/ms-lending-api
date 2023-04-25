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

package com.selina.lending.internal.repository;

import com.selina.lending.internal.api.SelectionServiceApi;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.quote.Application;
import com.selina.lending.internal.service.application.domain.quote.FilterQuickQuoteApplicationRequest;
import com.selina.lending.internal.service.application.domain.quote.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.internal.service.application.domain.quote.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SelectionServiceRepositoryTest {

    @Mock
    private SelectionServiceApi selectionServiceApi;

    @Mock
    private TokenService tokenService;

    @Mock
    private FilteredQuickQuoteDecisionResponse filteredQuickQuoteDecisionResponse;

    @Mock
    private FilterQuickQuoteApplicationRequest filterQuickQuoteApplicationRequest;

    @Mock
    private Application application;

    private SelectionServiceRepository selectionServiceRepository;

    @BeforeEach
    void setUp() {
        selectionServiceRepository = new SelectionServiceRepositoryImpl(selectionServiceApi, tokenService);
    }

    @Test
    void filterShouldCallHttpClient() {
        //Given
        var externalApplicationId = UUID.randomUUID().toString();
        var sourceAccount = "Broker";
        var partnerAccountId = "Partner";

        when(filterQuickQuoteApplicationRequest.getApplication()).thenReturn(application);
        when(selectionServiceApi.filterQuickQuote(any())).thenReturn(filteredQuickQuoteDecisionResponse);
        when(application.getExternalApplicationId()).thenReturn(externalApplicationId);
        when(tokenService.retrieveSourceAccount()).thenReturn(sourceAccount);
        when(tokenService.retrievePartnerAccountId()).thenReturn(partnerAccountId);

        //When
        selectionServiceRepository.filter(filterQuickQuoteApplicationRequest);

        //Then
        verify(selectionServiceApi, times(1)).filterQuickQuote(filterQuickQuoteApplicationRequest);
        verify(application, times(1)).setSource(any(Source.class));
        verify(application, times(1)).setPartnerAccountId(any(String.class));
    }

    @Test
    void whenPartnerAccountIdIsNullThenDoNotSpecifyInQuickQuoteApplicationRequest() {
        //Given
        var externalApplicationId = UUID.randomUUID().toString();
        var sourceAccount = "Broker";

        when(filterQuickQuoteApplicationRequest.getApplication()).thenReturn(application);
        when(selectionServiceApi.filterQuickQuote(any())).thenReturn(filteredQuickQuoteDecisionResponse);
        when(application.getExternalApplicationId()).thenReturn(externalApplicationId);
        when(tokenService.retrieveSourceAccount()).thenReturn(sourceAccount);
        when(tokenService.retrievePartnerAccountId()).thenReturn(null);

        //When
        selectionServiceRepository.filter(filterQuickQuoteApplicationRequest);

        //Then
        verify(application, never()).setPartnerAccountId(any());
    }
}