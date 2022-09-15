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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Status;

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

@ExtendWith(MockitoExtension.class)
class LendingServiceImplTest {
    private static final String APPLICATION_ID = "applicationId";
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";

    @Mock
    private MiddlewareRepository middlewareRepository;

    @Mock
    private ApplicationResponse applicationResponse;

    @InjectMocks
    private LendingServiceImpl lendingService;

    @Test
    void getApplication() {
        //Given
        var applicationDecisionResponse = Optional.of(
                ApplicationDecisionResponse.builder().id(APPLICATION_ID).externalApplicationId(EXTERNAL_APPLICATION_ID).build());
        when(middlewareRepository.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationDecisionResponse);

        //When
        var response = lendingService.getApplication(EXTERNAL_APPLICATION_ID);

        //Then
        assertThat(response.isPresent(), equalTo(true));
        assertThat(response.get(), equalTo(applicationDecisionResponse.get()));
        assertThat(response.get().getId(), equalTo(APPLICATION_ID));
        assertThat(response.get().getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
    }

    @Test
    void getApplicationWhenNotAuthorisedThrowsAccessDeniedException() {
        //Given
        when(middlewareRepository.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenThrow(new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE));

        //When
        var exception = assertThrows(AccessDeniedException.class, () -> lendingService.getApplication(EXTERNAL_APPLICATION_ID));

        //Then
        assertThat(exception.getMessage(), equalTo("Error processing request: Access denied for application"));
        assertThat(exception.getStatus(), equalTo(Status.FORBIDDEN));
    }

    @Test
    void updateDipApplication() {
        //Given
        var request = DIPApplicationRequest.builder().externalApplicationId(EXTERNAL_APPLICATION_ID).build();
        var requestArgumentCaptor = ArgumentCaptor.forClass(ApplicationRequest.class);
        when(middlewareRepository.updateDipApplicationById(eq(APPLICATION_ID), requestArgumentCaptor.capture())).thenReturn(applicationResponse);

        //When
        lendingService.updateDipApplication(APPLICATION_ID, request);

        //Then
        var requestValue = requestArgumentCaptor.getValue();
        assertThat(requestValue.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        verify(middlewareRepository, times(1)).updateDipApplicationById(eq(APPLICATION_ID), any());
    }

    @Test
    void updateDipApplicationWhenNotAuthorisedThrowsAccessDeniedException() {
        //Given
        var request = DIPApplicationRequest.builder().externalApplicationId(EXTERNAL_APPLICATION_ID).build();
        doThrow(new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE)).when(middlewareRepository).updateDipApplicationById(anyString(), any(ApplicationRequest.class));

        //When
        var exception = assertThrows(AccessDeniedException.class, () -> lendingService.updateDipApplication(APPLICATION_ID, request));

        //Then
        assertThat(exception.getMessage(), equalTo("Error processing request: Access denied for application"));
        assertThat(exception.getStatus(), equalTo(Status.FORBIDDEN));
    }

    @Test
    void createDipApplication() {
        //Given
        var request = DIPApplicationRequest.builder().externalApplicationId(EXTERNAL_APPLICATION_ID).build();
        when(middlewareRepository.createDipApplication(any())).thenReturn(applicationResponse);

        //When
        ApplicationResponse response = lendingService.createDipApplication(request);

        //Then
        assertThat(response, equalTo(applicationResponse));
        verify(middlewareRepository, times(1)).createDipApplication(any());
    }
}