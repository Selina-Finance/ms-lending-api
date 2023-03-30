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

package com.selina.lending.internal.service;

import com.selina.lending.api.errors.custom.ConflictException;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.Application;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.internal.service.application.domain.Offer;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateApplicationServiceImplTest {
    @Mock
    private MiddlewareRepository middlewareRepository;

    @Mock
    private MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;

    @Mock
    private ApplicationRequest applicationRequest;

    @Mock
    private ApplicationResponse applicationResponse;

    @Mock
    private ApplicationIdentifier applicationIdentifier;

    @Mock
    private DecisionMappingServiceImpl decisionMappingService;

    @Mock
    private Application mockApplication;

    @Mock
    private List<Offer> mockOffers;

    @InjectMocks
    private CreateApplicationServiceImpl createApplicationService;

    private Request request() {
        return Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate());
    }

    @Nested
    class CreateDipCCApplication {

        @Test
        void shouldCreateDipCCApplication() {
            //Given
            String notFoundMsg = "Not found";
            var id = UUID.randomUUID().toString();
            when(applicationRequest.getExternalApplicationId()).thenReturn(id);
            when(middlewareApplicationServiceRepository.getAppIdByExternalId(id)).thenThrow(
                    new FeignException.NotFound(notFoundMsg, request(), notFoundMsg.getBytes(), null));
            when(middlewareRepository.createDipCCApplication(applicationRequest)).thenReturn(applicationResponse);
            when(applicationResponse.getApplication()).thenReturn(mockApplication);
            when(mockApplication.getOffers()).thenReturn(mockOffers);

            //When
            var result = createApplicationService.createDipCCApplication(applicationRequest);

            //Then
            assertThat(result).isEqualTo(applicationResponse);
            verify(middlewareRepository, times(1)).createDipCCApplication(applicationRequest);
            verify(decisionMappingService, times(1)).mapDecision(mockOffers);
        }

        @Test
        void shouldThrowConflictExceptionWhenApplicationWithIdAlreadyExists() {
            //Given
            var id = UUID.randomUUID().toString();
            when(applicationRequest.getExternalApplicationId()).thenReturn(id);
            when(middlewareApplicationServiceRepository.getAppIdByExternalId(id)).thenReturn(applicationIdentifier);
            when(applicationIdentifier.getId()).thenReturn("any");

            //When
            var exception = assertThrows(ConflictException.class, () -> createApplicationService.createDipCCApplication(applicationRequest));

            //Then
            assertThat(exception.getMessage()).isEqualTo("Error processing request: Application already exists " + id);
            verify(middlewareRepository, times(0)).createDipCCApplication(applicationRequest);

        }

        @Test
        void shouldCreateDipCCApplicationWhenNoApplicationIdentifierIdReturned() {
            //Given
            var id = UUID.randomUUID().toString();
            when(applicationRequest.getExternalApplicationId()).thenReturn(id);
            when(middlewareApplicationServiceRepository.getAppIdByExternalId(id)).thenReturn(applicationIdentifier);
            when(applicationIdentifier.getId()).thenReturn(null);
            when(middlewareRepository.createDipCCApplication(applicationRequest)).thenReturn(applicationResponse);
            when(applicationResponse.getApplication()).thenReturn(mockApplication);
            when(mockApplication.getOffers()).thenReturn(mockOffers);

            //When
            createApplicationService.createDipCCApplication(applicationRequest);

            //Then
            verify(middlewareRepository, times(1)).createDipCCApplication(applicationRequest);
            verify(decisionMappingService,times(1)).mapDecision(mockOffers);
        }
    }

    @Nested
    class CreateDipApplication {
        @Test
        void shouldCreateDipApplication() {
            //Given
            String notFoundMsg = "Not found";
            var id = UUID.randomUUID().toString();
            when(applicationRequest.getExternalApplicationId()).thenReturn(id);
            when(middlewareApplicationServiceRepository.getAppIdByExternalId(id)).thenThrow(
                    new FeignException.NotFound(notFoundMsg, request(), notFoundMsg.getBytes(), null));
            when(middlewareRepository.createDipApplication(applicationRequest)).thenReturn(applicationResponse);
            when(applicationResponse.getApplication()).thenReturn(mockApplication);
            when(mockApplication.getOffers()).thenReturn(mockOffers);

            //When
            var result = createApplicationService.createDipApplication(applicationRequest);

            //Then
            assertThat(result).isEqualTo(applicationResponse);
            verify(middlewareRepository, times(1)).createDipApplication(applicationRequest);
            verify(decisionMappingService, times(1)).mapDecision(mockOffers);
        }

        @Test
        void shouldThrowConflictExceptionWhenApplicationWithIdAlreadyExists() {
            //Given
            var id = UUID.randomUUID().toString();
            when(applicationRequest.getExternalApplicationId()).thenReturn(id);
            when(middlewareApplicationServiceRepository.getAppIdByExternalId(id)).thenReturn(applicationIdentifier);
            when(applicationIdentifier.getId()).thenReturn("any");

            //When
            var exception = assertThrows(ConflictException.class, () -> createApplicationService.createDipApplication(applicationRequest));

            //Then
            assertThat(exception.getMessage()).isEqualTo("Error processing request: Application already exists " + id);
            verify(middlewareRepository, times(0)).createDipApplication(applicationRequest);
        }
    }
}