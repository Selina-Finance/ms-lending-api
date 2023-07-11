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
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.repository.GetApplicationRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.httpclient.getapplication.dto.response.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.internal.service.application.domain.Application;
import com.selina.lending.internal.service.application.domain.Offer;
import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFRequest;
import com.selina.lending.internal.service.filter.RuleOutcomeFilter;

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

import static com.selina.lending.internal.dto.LendingConstants.ACCEPT_DECISION;

@ExtendWith(MockitoExtension.class)
class CreateApplicationServiceImplTest extends MapperBase {
    @Mock
    private MiddlewareRepository middlewareRepository;

    @Mock
    private GetApplicationRepository getApplicationRepository;

    @Mock
    private QuickQuoteCFRequest quickQuoteCFRequest;

    @Mock
    private ApplicationRequest applicationRequest;

    @Mock
    private ApplicationResponse applicationResponse;

    @Mock
    private ApplicationIdentifier applicationIdentifier;

    @Mock
    private Application application;

    @Mock
    private List<Offer> offers;

    @Mock
    private RuleOutcomeFilter ruleOutcomeFilter;

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
            when(getApplicationRepository.getAppIdByExternalId(id)).thenThrow(
                    new FeignException.NotFound(notFoundMsg, request(), notFoundMsg.getBytes(), null));
            when(middlewareRepository.createDipCCApplication(applicationRequest)).thenReturn(applicationResponse);
            when(applicationResponse.getApplication()).thenReturn(application);
            when(applicationResponse.getApplicationType()).thenReturn(DIP_APPLICATION_TYPE);
            when(application.getDecision()).thenReturn(ACCEPT_DECISION);
            when(application.getOffers()).thenReturn(offers);

            //When
            var result = createApplicationService.createDipCCApplication(applicationRequest);

            //Then
            assertThat(result).isEqualTo(applicationResponse);
            verify(middlewareRepository, times(1)).createDipCCApplication(applicationRequest);
            verify(ruleOutcomeFilter, times(1)).filterOfferRuleOutcomes(ACCEPT_DECISION, DIP_APPLICATION_TYPE , offers);
        }

        @Test
        void shouldThrowConflictExceptionWhenApplicationWithIdAlreadyExists() {
            //Given
            var id = UUID.randomUUID().toString();
            when(applicationRequest.getExternalApplicationId()).thenReturn(id);
            when(getApplicationRepository.getAppIdByExternalId(id)).thenReturn(applicationIdentifier);
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
            when(getApplicationRepository.getAppIdByExternalId(id)).thenReturn(applicationIdentifier);
            when(applicationIdentifier.getId()).thenReturn(null);
            when(applicationResponse.getApplication()).thenReturn(application);
            when(middlewareRepository.createDipCCApplication(applicationRequest)).thenReturn(applicationResponse);
            when(applicationResponse.getApplication()).thenReturn(application);
            when(applicationResponse.getApplicationType()).thenReturn(DIP_APPLICATION_TYPE);
            when(application.getDecision()).thenReturn(ACCEPT_DECISION);
            when(application.getOffers()).thenReturn(offers);

            //When
            createApplicationService.createDipCCApplication(applicationRequest);

            //Then
            verify(middlewareRepository, times(1)).createDipCCApplication(applicationRequest);
            verify(ruleOutcomeFilter, times(1)).filterOfferRuleOutcomes(ACCEPT_DECISION, DIP_APPLICATION_TYPE, offers);
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
            when(getApplicationRepository.getAppIdByExternalId(id)).thenThrow(
                    new FeignException.NotFound(notFoundMsg, request(), notFoundMsg.getBytes(), null));
            when(middlewareRepository.createDipApplication(applicationRequest)).thenReturn(applicationResponse);
            when(applicationResponse.getApplication()).thenReturn(application);
            when(application.getDecision()).thenReturn(ACCEPT_DECISION);
            when(applicationResponse.getApplicationType()).thenReturn(DIP_APPLICATION_TYPE);
            when(application.getOffers()).thenReturn(offers);

            //When
            var result = createApplicationService.createDipApplication(applicationRequest);

            //Then
            assertThat(result).isEqualTo(applicationResponse);
            verify(middlewareRepository, times(1)).createDipApplication(applicationRequest);
            verify(ruleOutcomeFilter, times(1)).filterOfferRuleOutcomes(ACCEPT_DECISION, DIP_APPLICATION_TYPE, offers);
        }

        @Test
        void shouldThrowConflictExceptionWhenApplicationWithIdAlreadyExists() {
            //Given
            var id = UUID.randomUUID().toString();
            when(applicationRequest.getExternalApplicationId()).thenReturn(id);
            when(getApplicationRepository.getAppIdByExternalId(id)).thenReturn(applicationIdentifier);
            when(applicationIdentifier.getId()).thenReturn("any");

            //When
            var exception = assertThrows(ConflictException.class, () -> createApplicationService.createDipApplication(applicationRequest));

            //Then
            assertThat(exception.getMessage()).isEqualTo("Error processing request: Application already exists " + id);
            verify(middlewareRepository, times(0)).createDipApplication(applicationRequest);
        }
    }

    @Nested
    class CreateQuickQuoteCFApplication {

        @Test
        void shouldCreateQuickQuoteCFApplication() {
            //Given
            var expectedQuickQuoteCFResponse = getQuickQuoteCFResponse();

            when(middlewareRepository.createQuickQuoteCFApplication(quickQuoteCFRequest)).thenReturn(expectedQuickQuoteCFResponse);

            //When
            var quickQuoteCFResponse = createApplicationService.createQuickQuoteCFApplication(quickQuoteCFRequest);

            //Then
            assertThat(quickQuoteCFResponse).isEqualTo(expectedQuickQuoteCFResponse);
        }

        @Test
        void whenCreateQuickQuoteCFApplicationThenFilterOutDeclinedOffers() {
            //Given
            var quickQuoteCFResponse = getQuickQuoteCFResponse();
            var acceptedOffer = getOffer(OFFER_DECISION_ACCEPT);
            var declinedOffer = getOffer(OFFER_DECISION_DECLINE);
            var declinedOffer2 = getOffer("decline");
            quickQuoteCFResponse.setOffers(List.of(acceptedOffer, declinedOffer, declinedOffer2));

            when(middlewareRepository.createQuickQuoteCFApplication(quickQuoteCFRequest)).thenReturn(quickQuoteCFResponse);

            //When
            var filteredQuickQuoteCFResponse = createApplicationService.createQuickQuoteCFApplication(quickQuoteCFRequest);

            //Then
            assertThat(filteredQuickQuoteCFResponse.getOffers()).containsExactly(acceptedOffer);
        }
    }
}