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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;

@ExtendWith(MockitoExtension.class)
class RetrieveApplicationServiceImplTest {
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";
    private static final String SOURCE_ACCOUNT = "sourceAccount";
    private static final String ANOTHER_SOURCE_ACCOUNT = "anotherSourceAccount";
    private static final String ACCESS_DENIED_MSG =
            "Error processing request: Access denied for application " + EXTERNAL_APPLICATION_ID;

    @Mock
    private ApplicationIdentifier applicationIdentifier;
    @Mock
    private MiddlewareRepository middlewareRepository;

    @Mock
    private MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private RetrieveApplicationServiceImpl retrieveApplicationService;

    @Test
    void shouldGetApplicationByExternalApplicationId() {
        //Given
        when(middlewareApplicationServiceRepository.getApplicationSourceAccountByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(middlewareApplicationServiceRepository.getApplicationIdByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getId()).thenReturn(EXTERNAL_APPLICATION_ID);

        //When
        retrieveApplicationService.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID);

        //Then
        verify(middlewareRepository, times(1)).getApplicationById(EXTERNAL_APPLICATION_ID);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenNotAuthorisedToGetApplication() {
        //Given
        when(middlewareApplicationServiceRepository.getApplicationSourceAccountByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(tokenService.retrieveSourceAccount()).thenReturn(ANOTHER_SOURCE_ACCOUNT);

        //When
        var exception = assertThrows(AccessDeniedException.class,
                () -> retrieveApplicationService.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID));

        //Then
        assertThat(exception.getMessage()).isEqualTo(ACCESS_DENIED_MSG);
        verify(middlewareRepository, times(0)).getApplicationById(EXTERNAL_APPLICATION_ID);
    }
}