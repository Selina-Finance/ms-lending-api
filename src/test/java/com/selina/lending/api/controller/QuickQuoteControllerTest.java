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
import com.selina.lending.internal.service.FilterApplicationService;
import com.selina.lending.internal.service.application.domain.quote.FilterQuickQuoteApplicationRequest;
import com.selina.lending.internal.service.application.domain.quote.FilteredQuickQuoteDecisionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private FilteredQuickQuoteDecisionResponse filteredQuickQuoteDecisionResponse;

    @Mock
    private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

    @Test
    void createQuickQuoteApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        when(quickQuoteApplicationRequest.getExternalApplicationId()).thenReturn(id);
        when(filterApplicationService.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(filteredQuickQuoteDecisionResponse);

        //When
        var response = quickQuoteController.createQuickQuoteApplication(quickQuoteApplicationRequest);

        //Then
        assertNotNull(response);
        assertThat(Objects.requireNonNull(response.getBody()).getExternalApplicationId(), equalTo(id));
        verify(filterApplicationService, times(1)).filter(any());
    }

    @Test
    void updateQuickQuoteApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        when(quickQuoteApplicationRequest.getExternalApplicationId()).thenReturn(id);
        when(filterApplicationService.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(filteredQuickQuoteDecisionResponse);

        //When
        var response = quickQuoteController.updateQuickQuoteApplication(id, quickQuoteApplicationRequest);

        //Then
        assertNotNull(response);
        assertThat(Objects.requireNonNull(response.getBody()).getExternalApplicationId(), equalTo(id));
        verify(filterApplicationService, times(1)).filter(any());
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