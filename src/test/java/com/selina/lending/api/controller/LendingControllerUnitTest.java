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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.dto.QuickQuoteApplicationRequest;
import com.selina.lending.internal.service.CreateApplicationService;
import com.selina.lending.internal.service.RetrieveApplicationService;
import com.selina.lending.internal.service.UpdateApplicationService;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

@ExtendWith(MockitoExtension.class)
class LendingControllerUnitTest {

    private static final String APPLICATION_ID = UUID.randomUUID().toString();

    @Mock
    private ApplicationDecisionResponse mwApplicationDecisionResponse;

    @Mock
    private ApplicationResponse mwApplicationResponse;

    @Mock
    private DIPApplicationRequest dipApplicationRequest;

    @Mock
    private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

    @Mock
    private RetrieveApplicationService retrieveApplicationService;

    @Mock
    private UpdateApplicationService updateApplicationService;

    @Mock
    private CreateApplicationService createApplicationService;

    @InjectMocks
    private LendingController lendingController;

    @Test
    void getApplication() {
        //Given
        when(retrieveApplicationService.getApplicationByExternalApplicationId(APPLICATION_ID)).thenReturn(Optional.of(mwApplicationDecisionResponse));

        //When
        lendingController.getApplication(APPLICATION_ID);

        //Then
        verify(retrieveApplicationService, times(1)).getApplicationByExternalApplicationId(APPLICATION_ID);
    }

    @Test
    void createDipApplication() {
        //Given
        when(createApplicationService.createDipApplication(any())).thenReturn(mwApplicationResponse);

        //When
        lendingController.createDipApplication(dipApplicationRequest);

        //Then
        verify(createApplicationService, times(1)).createDipApplication(any());
    }

    @Test
    void updateDipApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        var applicationType = "DIP";
        var mwApplicationResponse = ApplicationResponse.builder().applicationId(id).applicationType(applicationType).build();
        when(updateApplicationService.updateDipApplication(eq(APPLICATION_ID), any())).thenReturn(mwApplicationResponse);

        //When
        var appResponse = lendingController.updateDipApplication(APPLICATION_ID, dipApplicationRequest);

        //Then
        assertThat(Objects.requireNonNull(appResponse.getBody()).getRequestType(), equalTo(applicationType));
        assertThat(appResponse.getBody().getApplicationId(), equalTo(id));
        verify(updateApplicationService, times(1)).updateDipApplication(eq(APPLICATION_ID), any());
    }

    @Test
    void createQuickQuoteApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        when(quickQuoteApplicationRequest.getExternalApplicationId()).thenReturn(id);

        //When
        var response = lendingController.createQuickQuoteApplication(quickQuoteApplicationRequest);

        //Then
        assertNotNull(response);
    }

    @Test
    void updateQuickQuoteApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        when(quickQuoteApplicationRequest.getExternalApplicationId()).thenReturn(id);

        //When
        var response = lendingController.updateQuickQuoteApplication(id, quickQuoteApplicationRequest);

        //Then
        assertNotNull(response);
    }
}