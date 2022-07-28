/*
 *   Copyright 2022 Selina Finance
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package com.selina.lending.internal.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

@ExtendWith(MockitoExtension.class)
public class LendingServiceImplTest {
    private static final String APPLICATION_ID = "applicationId";
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";

    @Mock
    private MiddlewareRepository middlewareRepository;

    @Mock
    private ApplicationResponse applicationResponse;

    @InjectMocks
    private LendingServiceImpl lendingService;

    @Test
    public void getApplication() {
        //Given
        Optional<ApplicationDecisionResponse> applicationDecisionResponse = Optional.of(
                ApplicationDecisionResponse.builder().id(APPLICATION_ID).build());

        when(middlewareRepository.getApplicationById(eq(APPLICATION_ID))).thenReturn(applicationDecisionResponse);

        //When
        Optional<ApplicationDecisionResponse> response = lendingService.getApplication(APPLICATION_ID);

        //Then
        assertThat(response.isPresent(), equalTo(true));
        assertThat(response.get(), equalTo(applicationDecisionResponse.get()));
        assertThat(response.get().getId(), equalTo(APPLICATION_ID));
        verify(middlewareRepository, times(1)).getApplicationById(eq(APPLICATION_ID));
    }

    @Test
    public void updateDipApplication() {
        //Given
        DIPApplicationRequest request = DIPApplicationRequest.builder().externalApplicationId(EXTERNAL_APPLICATION_ID).build();
        when(middlewareRepository.updateDipApplication(eq(APPLICATION_ID), any(ApplicationRequest.class))).thenReturn(applicationResponse);

        //When
        ApplicationResponse response = lendingService.updateDipApplication(APPLICATION_ID, request);

        //Then
        assertThat(response, equalTo(applicationResponse));
        verify(middlewareRepository, times(1)).updateDipApplication(eq(APPLICATION_ID), any());
    }

    @Test
    public void createDipApplication() {
        //Given
        DIPApplicationRequest request = DIPApplicationRequest.builder().externalApplicationId(EXTERNAL_APPLICATION_ID).build();
        when(middlewareRepository.createDipApplication(any())).thenReturn(applicationResponse);

        //When
        ApplicationResponse response = lendingService.createDipApplication(request);

        //Then
        assertThat(response, equalTo(applicationResponse));
        verify(middlewareRepository, times(1)).createDipApplication(any());
    }
}