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

import com.selina.lending.api.dto.dip.request.DIPApplicationRequest;
import com.selina.lending.api.dto.dipcc.request.DIPCCApplicationRequest;
import com.selina.lending.httpclient.middleware.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.httpclient.middleware.dto.dip.response.ApplicationResponse;
import com.selina.lending.service.CreateApplicationService;
import com.selina.lending.service.RetrieveApplicationService;
import com.selina.lending.service.UpdateApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DIPControllerUnitTest {

    private static final String APPLICATION_ID = UUID.randomUUID().toString();

    @Mock
    private ApplicationDecisionResponse mwApplicationDecisionResponse;

    @Mock
    private ApplicationResponse mwApplicationResponse;

    @Mock
    private DIPApplicationRequest dipApplicationRequest;


    @Mock
    private DIPCCApplicationRequest dipccApplicationRequest;

    @Mock
    private RetrieveApplicationService retrieveApplicationService;

    @Mock
    private UpdateApplicationService updateApplicationService;

    @Mock
    private CreateApplicationService createApplicationService;

    @InjectMocks
    private DIPController lendingController;

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
    void createDipCCApplication() {
        //Given
        when(createApplicationService.createDipCCApplication(any())).thenReturn(mwApplicationResponse);

        //When
        lendingController.createDipCCApplication(dipccApplicationRequest);

        //Then
        verify(createApplicationService, times(1)).createDipCCApplication(any());
    }

    @Test
    void updateDipCCApplication() {
        //Given
        doNothing().when(updateApplicationService).updateDipCCApplication(eq(APPLICATION_ID), any());

        //When
        lendingController.updateDipCCApplication(APPLICATION_ID, dipccApplicationRequest);

        //Then
        verify(updateApplicationService, times(1)).updateDipCCApplication(eq(APPLICATION_ID), any());
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
        doNothing().when(updateApplicationService).updateDipApplication(eq(APPLICATION_ID), any());

        //When
        lendingController.updateDipApplication(APPLICATION_ID, dipApplicationRequest);

        //Then
        verify(updateApplicationService, times(1)).updateDipApplication(eq(APPLICATION_ID), any());
    }
}