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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.internal.service.application.domain.SelectProductResponse;

@ExtendWith(MockitoExtension.class)
class UpdateApplicationServiceImplTest {
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";
    private static final String SOURCE_ACCOUNT = "sourceAccount";
    private static final String ACCESS_DENIED_MSG =
            "Error processing request: Access denied for application " + EXTERNAL_APPLICATION_ID;

    @Mock
    private ApplicationIdentifier applicationIdentifier;

    @Mock
    private ApplicationResponse applicationResponse;

    @Mock
    private ApplicationRequest applicationRequest;

    @Mock
    private MiddlewareRepository middlewareRepository;

    @Mock
    private MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;

    @Mock
    private AccessManagementService accessManagementService;

    @InjectMocks
    private UpdateApplicationServiceImpl updateApplicationService;

    @Test
    void shouldUpdateDipApplication() {
        //Given
        when(middlewareApplicationServiceRepository.getApplicationSourceAccountByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(accessManagementService.isSourceAccountAccessAllowed(SOURCE_ACCOUNT)).thenReturn(true);
        when(applicationRequest.getExternalApplicationId()).thenReturn(EXTERNAL_APPLICATION_ID);
        when(middlewareRepository.createDipApplication(any())).thenReturn(applicationResponse);
        doNothing().when(middlewareApplicationServiceRepository).deleteApplicationByExternalApplicationId(
                SOURCE_ACCOUNT, EXTERNAL_APPLICATION_ID);

        //When
        updateApplicationService.updateDipApplication(EXTERNAL_APPLICATION_ID, applicationRequest);

        //Then
        verify(middlewareApplicationServiceRepository, times(1)).getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID);
        verify(middlewareRepository, times(1)).createDipApplication(any());
        verify(middlewareApplicationServiceRepository, times(1)).deleteApplicationByExternalApplicationId(
                SOURCE_ACCOUNT, EXTERNAL_APPLICATION_ID);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenNotAuthorisedToUpdateApplication() {
        // Given
        when(middlewareApplicationServiceRepository.getApplicationSourceAccountByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(accessManagementService.isSourceAccountAccessAllowed(SOURCE_ACCOUNT)).thenReturn(false);

        // When
        var exception = assertThrows(AccessDeniedException.class,
                () -> updateApplicationService.updateDipApplication(EXTERNAL_APPLICATION_ID, applicationRequest));

        // Then
        assertThat(exception.getMessage()).isEqualTo(ACCESS_DENIED_MSG);
        verify(middlewareApplicationServiceRepository, times(1)).getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID);
        verify(middlewareRepository, times(0)).createDipApplication(applicationRequest);
        verify(middlewareApplicationServiceRepository, times(0)).deleteApplicationByExternalApplicationId(
                SOURCE_ACCOUNT, EXTERNAL_APPLICATION_ID);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenApplicationRequestExternalApplicationIdNotMatched() {
        // Given
        when(middlewareApplicationServiceRepository.getApplicationSourceAccountByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(accessManagementService.isSourceAccountAccessAllowed(SOURCE_ACCOUNT)).thenReturn(true);
        when(applicationRequest.getExternalApplicationId()).thenReturn("any id");

        // When
        var exception = assertThrows(AccessDeniedException.class,
                () -> updateApplicationService.updateDipApplication(EXTERNAL_APPLICATION_ID, applicationRequest));

        // Then
        assertThat(exception.getMessage()).isEqualTo(ACCESS_DENIED_MSG);
        verify(middlewareApplicationServiceRepository, times(1)).getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID);
        verify(middlewareRepository, times(0)).createDipApplication(applicationRequest);
        verify(middlewareApplicationServiceRepository, times(0)).deleteApplicationByExternalApplicationId(
                SOURCE_ACCOUNT, EXTERNAL_APPLICATION_ID);
    }

    @Test
    void shouldSuccessfullySelectProduct() {
        //Given
        var productCode = "PR01";
        var appId = UUID.randomUUID().toString();
        var selectProductResponse = SelectProductResponse.builder().id("appId").message("success").build();
        when(middlewareApplicationServiceRepository.getApplicationIdByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getId()).thenReturn(appId);
        when(middlewareApplicationServiceRepository.getApplicationSourceAccountByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        doNothing().when(accessManagementService).checkSourceAccountAccessPermitted(SOURCE_ACCOUNT);
        when(middlewareRepository.selectProduct(appId, productCode)).thenReturn(selectProductResponse);

        //When
        var response = updateApplicationService.selectProductOffer(EXTERNAL_APPLICATION_ID, productCode);

        //Then
        assertThat(response).isEqualTo(selectProductResponse);
        verify(middlewareApplicationServiceRepository, times(1)).getApplicationIdByExternalApplicationId(EXTERNAL_APPLICATION_ID);
        verify(middlewareApplicationServiceRepository, times(1)).getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID);
        verify(middlewareRepository, times(1)).selectProduct(appId, productCode);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenNotAuthorisedToSelectProduct() {
        //Given
        var productCode = "PR01";
        var appId = UUID.randomUUID().toString();
        when(middlewareApplicationServiceRepository.getApplicationIdByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(middlewareApplicationServiceRepository.getApplicationSourceAccountByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn("not permitted");
        doThrow(new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE)).when(accessManagementService).checkSourceAccountAccessPermitted("not permitted");

        //When
        var exception = assertThrows(AccessDeniedException.class,
                () -> updateApplicationService.selectProductOffer(EXTERNAL_APPLICATION_ID, productCode) );

        //Then
        assertThat(exception.getStatus().getReasonPhrase()).isEqualTo(HttpStatus.FORBIDDEN.getReasonPhrase());
        verify(middlewareApplicationServiceRepository, times(1)).getApplicationIdByExternalApplicationId(EXTERNAL_APPLICATION_ID);
        verify(middlewareApplicationServiceRepository, times(1)).getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID);
        verify(middlewareRepository, times(0)).selectProduct(appId, productCode);
    }
}