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

import com.selina.lending.exception.AccessDeniedException;
import com.selina.lending.repository.GetApplicationRepository;
import com.selina.lending.repository.MiddlewareRepository;
import com.selina.lending.httpclient.middleware.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.httpclient.getapplication.dto.response.ApplicationIdentifier;
import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.internal.service.filter.RuleOutcomeFilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.selina.lending.internal.dto.LendingConstants.ACCEPT_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.DIP_APPLICATION_TYPE;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RetrieveApplicationServiceImplTest {
    private static final String APPLICATION_ID = "appId";
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";
    private static final String SOURCE_ACCOUNT = "sourceAccount";
    private static final String ACCESS_DENIED_MSG = "Error processing request: Access denied for application";

    @Mock
    private ApplicationIdentifier applicationIdentifier;


    @Mock
    private ApplicationDecisionResponse applicationDecisionResponse;

    @Mock
    private List<Offer> offers;
    @Mock
    private MiddlewareRepository middlewareRepository;

    @Mock
    private GetApplicationRepository getApplicationRepository;

    @Mock
    private AccessManagementService accessManagementService;

    @Mock
    private RuleOutcomeFilter ruleOutcomeFilter;


    @InjectMocks
    private RetrieveApplicationServiceImpl retrieveApplicationService;

    @Test
    void shouldGetApplicationByExternalApplicationId() {
        //Given
        when(getApplicationRepository.getAppIdByExternalId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(applicationIdentifier.getId()).thenReturn(APPLICATION_ID);
        doNothing().when(accessManagementService).checkSourceAccountAccessPermitted(SOURCE_ACCOUNT);
        when(middlewareRepository.getApplicationById(APPLICATION_ID)).thenReturn(Optional.of(applicationDecisionResponse));
        when(applicationDecisionResponse.getApplicationType()).thenReturn(DIP_APPLICATION_TYPE);
        when(applicationDecisionResponse.getDecision()).thenReturn(ACCEPT_DECISION);
        when(applicationDecisionResponse.getOffers()).thenReturn(offers);

        //When
        retrieveApplicationService.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID);

        //Then
        verify(middlewareRepository, times(1)).getApplicationById(APPLICATION_ID);
        verify(ruleOutcomeFilter, times(1)).filterOfferRuleOutcomes(ACCEPT_DECISION, DIP_APPLICATION_TYPE, offers);
    }

    @Test
    void shouldGetApplicationByExternalApplicationIdForQuickQuote() {
        //Given
        when(getApplicationRepository.getAppIdByExternalId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(applicationIdentifier.getId()).thenReturn(APPLICATION_ID);
        doNothing().when(accessManagementService).checkSourceAccountAccessPermitted(SOURCE_ACCOUNT);
        when(middlewareRepository.getApplicationById(APPLICATION_ID)).thenReturn(Optional.of(applicationDecisionResponse));
        when(applicationDecisionResponse.getDecision()).thenReturn(ACCEPT_DECISION);
        when(applicationDecisionResponse.getApplicationType()).thenReturn("QuickQuote");
        when(applicationDecisionResponse.getOffers()).thenReturn(offers);

        //When
        retrieveApplicationService.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID);

        //Then
        verify(middlewareRepository, times(1)).getApplicationById(APPLICATION_ID);
        verify(ruleOutcomeFilter, times(1)).filterOfferRuleOutcomes(ACCEPT_DECISION, "QuickQuote", offers);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenNotAuthorisedToGetApplication() {
        //Given
        when(getApplicationRepository.getAppIdByExternalId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        doThrow(new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE)).when(accessManagementService).checkSourceAccountAccessPermitted(SOURCE_ACCOUNT);

        //When
        var exception = assertThrows(AccessDeniedException.class,
                () -> retrieveApplicationService.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID));

        //Then
        assertThat(exception.getMessage()).isEqualTo(ACCESS_DENIED_MSG);
        verify(middlewareRepository, times(0)).getApplicationById(APPLICATION_ID);
    }
}