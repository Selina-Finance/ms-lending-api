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

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.dto.quotecf.QuickQuoteCFApplicationRequest;
import com.selina.lending.internal.enricher.ApplicationResponseEnricher;
import com.selina.lending.internal.service.CreateApplicationService;
import com.selina.lending.internal.service.FilterApplicationService;
import com.selina.lending.internal.service.application.domain.quote.selection.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFRequest;
import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuickQuoteControllerTest {

    @InjectMocks
    private QuickQuoteController quickQuoteController;

    @Mock
    private FilterApplicationService filterApplicationService;

    @Mock
    private CreateApplicationService createApplicationService;

    @Spy
    private ApplicationResponseEnricher applicationResponseEnricher = new ApplicationResponseEnricher("");

    @Mock
    private FilteredQuickQuoteDecisionResponse filteredQuickQuoteDecisionResponse;

    @Mock
    private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

    @Mock
    private QuickQuoteCFApplicationRequest quickQuoteCFApplicationRequest;

    @Mock
    private QuickQuoteCFResponse quickQuoteCFResponse;

    @Test
    void createQuickQuoteApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        when(quickQuoteApplicationRequest.getExternalApplicationId()).thenReturn(id);
        when(filterApplicationService.filter(quickQuoteApplicationRequest)).thenReturn(filteredQuickQuoteDecisionResponse);

        //When
        var response = quickQuoteController.createQuickQuoteApplication(quickQuoteApplicationRequest);

        //Then
        assertNotNull(response);
        assertThat(Objects.requireNonNull(response.getBody()).getExternalApplicationId(), equalTo(id));
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
        when(filterApplicationService.filter(quickQuoteApplicationRequest)).thenReturn(filteredQuickQuoteDecisionResponse);

        //When
        var response = quickQuoteController.updateQuickQuoteApplication(id, quickQuoteApplicationRequest);

        //Then
        assertNotNull(response);
        assertThat(Objects.requireNonNull(response.getBody()).getExternalApplicationId(), equalTo(id));
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
}