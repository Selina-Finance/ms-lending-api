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

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.repository.GetApplicationRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.httpclient.getapplication.dto.response.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.SelectProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private static final String PRODUCT_CODE = "PR01";
    private static final String APPLICATION_ID = UUID.randomUUID().toString();
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";
    private static final String SOURCE_ACCOUNT = "sourceAccount";

    @Mock
    private ApplicationIdentifier applicationIdentifier;

    @Mock
    private MiddlewareRepository middlewareRepository;

    @Mock
    private GetApplicationRepository getApplicationRepository;

    @Mock
    private AccessManagementService accessManagementService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldSuccessfullySelectProduct() {
        //Given
        var selectProductResponse = SelectProductResponse.builder().id("appId").message("success").build();
        when(getApplicationRepository.getAppIdByExternalId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getId()).thenReturn(APPLICATION_ID);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        doNothing().when(accessManagementService).checkSourceAccountAccessPermitted(SOURCE_ACCOUNT);
        when(middlewareRepository.selectProduct(APPLICATION_ID, PRODUCT_CODE)).thenReturn(selectProductResponse);

        //When
        var response = productService.selectProductOffer(EXTERNAL_APPLICATION_ID, PRODUCT_CODE);

        //Then
        assertThat(response).isEqualTo(selectProductResponse);
        verify(getApplicationRepository, times(1)).getAppIdByExternalId(EXTERNAL_APPLICATION_ID);
        verify(middlewareRepository, times(1)).selectProduct(APPLICATION_ID, PRODUCT_CODE);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenNotAuthorisedToSelectProduct() {
        //Given
        when(getApplicationRepository.getAppIdByExternalId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn("not permitted");
        doThrow(new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE)).when(accessManagementService).checkSourceAccountAccessPermitted("not permitted");

        //When
        var exception = assertThrows(AccessDeniedException.class,
                () -> productService.selectProductOffer(EXTERNAL_APPLICATION_ID, PRODUCT_CODE));

        //Then
        assertThat(exception.getStatus().getReasonPhrase()).isEqualTo(HttpStatus.FORBIDDEN.getReasonPhrase());
        verify(getApplicationRepository, times(1)).getAppIdByExternalId(EXTERNAL_APPLICATION_ID);
        verify(middlewareRepository, times(0)).selectProduct(APPLICATION_ID, PRODUCT_CODE);
    }
}