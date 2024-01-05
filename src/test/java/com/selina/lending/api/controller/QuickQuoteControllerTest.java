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

package com.selina.lending.api.controller;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.dto.qq.response.ProductOfferDto;
import com.selina.lending.api.dto.qq.response.QuickQuoteResponse;
import com.selina.lending.api.dto.qqcf.request.QuickQuoteCFApplicationRequest;
import com.selina.lending.exception.AccessDeniedException;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.response.QuickQuoteCFResponse;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.service.CreateApplicationService;
import com.selina.lending.service.FilterApplicationService;
import com.selina.lending.service.TokenService;
import com.selina.lending.service.enricher.ApplicationResponseEnricher;
import com.selina.lending.testutil.ProductHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuickQuoteControllerTest {
    private QuickQuoteController quickQuoteController;

    @Mock
    private FilterApplicationService filterApplicationService;

    @Mock
    private CreateApplicationService createApplicationService;

    @Mock
    private TokenService tokenService;

    private ApplicationResponseEnricher applicationResponseEnricher;

    @Mock
    private FilteredQuickQuoteDecisionResponse filteredQuickQuoteDecisionResponse;

    @Mock
    private QuickQuoteResponse quickQuoteResponse;

    @Mock
    private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

    @Mock
    private QuickQuoteCFApplicationRequest quickQuoteCFApplicationRequest;

    @Mock
    private QuickQuoteCFResponse quickQuoteCFResponse;

    private static final String QUICK_QUOTE_URL = "https://mf-quick-quote";

    @BeforeEach
    void setUp() {
        applicationResponseEnricher = Mockito.spy(new ApplicationResponseEnricher(QUICK_QUOTE_URL, tokenService));

        quickQuoteController = new QuickQuoteController(filterApplicationService, createApplicationService,
                applicationResponseEnricher);
    }

    @Test
    void createQuickQuoteApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        when(quickQuoteApplicationRequest.getExternalApplicationId()).thenReturn(id);
        when(filterApplicationService.filter(quickQuoteApplicationRequest)).thenReturn(quickQuoteResponse);
        when(quickQuoteResponse.getExternalApplicationId()).thenReturn(id);
        when(quickQuoteResponse.getOffers()).thenReturn(buildProductList());
        when(tokenService.retrieveClientId()).thenReturn("clearscore");

        //When
        var response = quickQuoteController.createQuickQuoteApplication(quickQuoteApplicationRequest);

        //Then
        assertNotNull(response);
        assertThat(Objects.requireNonNull(response.getBody()).getExternalApplicationId(), equalTo(id));
        assertThat(Objects.requireNonNull(response.getBody()).getOffers(), not(empty()));
        assertOffersApplyUrlHasExpectedFormat(Objects.requireNonNull(response.getBody()).getOffers());
        verify(filterApplicationService, times(1)).filter(quickQuoteApplicationRequest);
    }

    @Test
    void createQuickQuoteCFApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        when(quickQuoteCFApplicationRequest.getExternalApplicationId()).thenReturn(id);
        when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(quickQuoteCFResponse);

        //When
        var response = quickQuoteController.createQuickQuoteCFApplication(quickQuoteCFApplicationRequest);

        //Then
        assertNotNull(response);
        assertThat(Objects.requireNonNull(response.getBody()).getExternalApplicationId(), equalTo(id));
        verify(createApplicationService, times(1)).createQuickQuoteCFApplication(any());
    }


    @Test
    void updateQuickQuoteApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        when(quickQuoteApplicationRequest.getExternalApplicationId()).thenReturn(id);
        when(filterApplicationService.filter(quickQuoteApplicationRequest)).thenReturn(quickQuoteResponse);
        when(quickQuoteResponse.getExternalApplicationId()).thenReturn(id);
        when(quickQuoteResponse.getOffers()).thenReturn(buildProductList());
        when(tokenService.retrieveClientId()).thenReturn("clearscore");

        //When
        var response = quickQuoteController.updateQuickQuoteApplication(id, quickQuoteApplicationRequest);

        //Then
        assertNotNull(response);
        assertThat(Objects.requireNonNull(response.getBody()).getExternalApplicationId(), equalTo(id));
        assertThat(Objects.requireNonNull(response.getBody()).getOffers(), not(empty()));
        assertOffersApplyUrlHasExpectedFormat(Objects.requireNonNull(response.getBody()).getOffers());
        verify(filterApplicationService, times(1)).filter(quickQuoteApplicationRequest);
    }

    @Test
    void updateQuickQuoteApplicationThrowsAccessDeniedException() {
        //Given
        var id = UUID.randomUUID().toString();
        when(quickQuoteApplicationRequest.getExternalApplicationId()).thenReturn(id);

        //When
        var exception = assertThrows(AccessDeniedException.class, () ->
                quickQuoteController.updateQuickQuoteApplication("anyId", quickQuoteApplicationRequest));

        //Then
        assertThat(exception.getMessage(), equalTo("Error processing request: Access denied for application anyId"));
    }

    private List<ProductOfferDto> buildProductList() {
        List<ProductOfferDto> list = new ArrayList<>();
        IntStream.range(0, 15).forEach(count -> list.add(ProductOfferDto.builder()
                .code(ProductHelper.getRandomProductCode())
                .build()
        ));

        return list;
    }

    private void assertOffersApplyUrlHasExpectedFormat(Collection<ProductOfferDto> productOffers) {
        final String APPLY_URL_REGEX = String.format("^%s/aggregator\\?externalApplicationId=.+&productCode=.+&source=.+$",
                QUICK_QUOTE_URL);

        for (ProductOfferDto offer : productOffers) {
            assertThat(offer.getApplyUrl(), matchesPattern(APPLY_URL_REGEX));
        }
    }
}