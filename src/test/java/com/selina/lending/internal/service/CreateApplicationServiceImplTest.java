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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.api.errors.custom.BadRequestException;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;

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

    @InjectMocks
    private CreateApplicationServiceImpl createApplicationService;

    @Test
    void shouldCreateDipApplication() {
        //Given
        String notFoundMsg = "Not found";
        var id = UUID.randomUUID().toString();
        when(applicationRequest.getExternalApplicationId()).thenReturn(id);
        when(middlewareApplicationServiceRepository.getApplicationIdByExternalApplicationId(id)).thenThrow(new FeignException.NotFound(notFoundMsg,
                request(), notFoundMsg.getBytes(), null));
        when(middlewareRepository.createDipApplication(applicationRequest)).thenReturn(applicationResponse);

        //When
        var result = createApplicationService.createDipApplication(applicationRequest);

        //Then
        assertThat(result).isEqualTo(applicationResponse);
        verify(middlewareRepository, times(1)).createDipApplication(applicationRequest);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenApplicationWithIdAlreadyExists() {
        //Given
        var id = UUID.randomUUID().toString();
        when(applicationRequest.getExternalApplicationId()).thenReturn(id);
        when(middlewareApplicationServiceRepository.getApplicationIdByExternalApplicationId(id)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getId()).thenReturn("any");

        //When
        var exception = assertThrows(BadRequestException.class,
                () ->  createApplicationService.createDipApplication(applicationRequest));

        //Then
        assertThat(exception.getMessage()).isEqualTo("Error processing request: Application already exists "+id);
        verify(middlewareRepository, times(0)).createDipApplication(applicationRequest);

    }

    private Request request() {
        return Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate());
    }
}