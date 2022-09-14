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

package com.selina.lending.internal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.api.MiddlewareApplicationServiceApi;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@ExtendWith(MockitoExtension.class)
class MiddlewareRepositoryTest {
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";
    private static final String ACCESS_DENIED_MSG =
            "Error processing request: Access denied for application "+ EXTERNAL_APPLICATION_ID;
    private static final String SOURCE_ACCOUNT = "source account";
    private static final String ANOTHER_SOURCE_ACCOUNT = "Another source account";

    @Mock
    private MiddlewareApi middlewareApi;

    @Mock
    private MiddlewareApplicationServiceApi middlewareApplicationServiceApi;

    @Mock
    private ApplicationRequest applicationRequest;

    @Mock
    private ApplicationIdentifier applicationIdentifier;

    private MiddlewareRepository middlewareRepository;

    @Mock
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        middlewareRepository = new MiddlewareRepositoryImpl(middlewareApi, middlewareApplicationServiceApi, tokenService);
    }

    @Test
    void shouldCallHttpClientWhenGetApplicationByExternalApplicationIdInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        var apiResponse = ApplicationDecisionResponse.builder().build();
        when(middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(middlewareApplicationServiceApi.getApplicationIdByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getId()).thenReturn(id);
        when(middlewareApi.getApplicationById(id)).thenReturn(apiResponse);

        // When
        var result = middlewareRepository.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID);

        // Then
        assertThat(result).isEqualTo(Optional.of(apiResponse));
    }

    @Test
    void shouldCallHttpClientWhenCreateApplicationInvoked() {
        // Given
        var apiResponse = ApplicationResponse.builder().build();

        when(middlewareApi.createDipApplication(applicationRequest)).thenReturn(apiResponse);

        // When
        var result = middlewareRepository.createDipApplication(applicationRequest);

        // Then
        assertThat(result).isEqualTo(apiResponse);
        verify(middlewareApi, times(1)).createDipApplication(applicationRequest);
    }

    @Test
    void shouldCallHttpClientWhenUpdateApplicationInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        when(applicationRequest.getExternalApplicationId()).thenReturn(EXTERNAL_APPLICATION_ID);
        when(middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        doNothing().when(middlewareApi).updateDipApplication(id, applicationRequest);

        // When
        middlewareRepository.updateDipApplicationById(id, applicationRequest);

        // Then
        verify(middlewareApi, times(1)).updateDipApplication(id, applicationRequest);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenNotAuthorisedToUpdateApplication() {
        // Given
        var id = UUID.randomUUID().toString();
        when(applicationRequest.getExternalApplicationId()).thenReturn(EXTERNAL_APPLICATION_ID);
        when(middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(tokenService.retrieveSourceAccount()).thenReturn(ANOTHER_SOURCE_ACCOUNT);

        // When
        var exception = assertThrows(AccessDeniedException.class, () -> middlewareRepository.updateDipApplicationById(id, applicationRequest));

        // Then
        assertThat(exception.getMessage()).isEqualTo(ACCESS_DENIED_MSG);
        verify(middlewareApi, times(0)).updateDipApplication(id, applicationRequest);
    }

    @Test
    void shouldThrowFeignServerExceptionWhenMiddlewareThrowsInternalServerException() {
        //Given
        String errorMsg = "error";
        var id = UUID.randomUUID().toString();
        when(middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(middlewareApplicationServiceApi.getApplicationIdByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getId()).thenReturn(id);

        //When
        when(middlewareApi.getApplicationById(id)).thenThrow(
                new FeignException.FeignServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg, createRequest(),
                        errorMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignServerException.class,
                () -> middlewareRepository.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID));

        //Then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
    }

    @Test
    void shouldThrowFeignClientExceptionWhenMiddlewareThrowsNotFoundException() {
        //Given
        String notFoundMsg = "not found";

        //When
        when(middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenThrow(
                new FeignException.FeignClientException(HttpStatus.NOT_FOUND.value(), notFoundMsg, createRequest(),
                        notFoundMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignClientException.class,
                () -> middlewareRepository.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID));

        //Then
        assertThat(exception.getMessage()).isEqualTo(notFoundMsg);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenSourceAccountNotMatchedTokenSourceAccount() {
        //Given
        String accessDeniedMsg = "Error processing request: Access denied for application "+ EXTERNAL_APPLICATION_ID;
        when(middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(ANOTHER_SOURCE_ACCOUNT);
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        var exception = assertThrows(AccessDeniedException.class,
                () -> middlewareRepository.getApplicationByExternalApplicationId(EXTERNAL_APPLICATION_ID));

        //Then
        assertThat(exception.getMessage()).isEqualTo(accessDeniedMsg);
        verify(middlewareApplicationServiceApi, times(0)).getApplicationIdByExternalApplicationId(EXTERNAL_APPLICATION_ID);
    }

    @Test
    void shouldOpenCircuitBreakerWhenFeignServerExceptionTriggersFallback() {
        //Given
        var id = UUID.randomUUID().toString();
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(id)).thenThrow(
                new FeignException.InternalServerError("Internal Server Error", createRequest(), "error".getBytes(), null));

        var supplier = circuitBreaker.decorateSupplier(() -> middlewareRepository.getApplicationByExternalApplicationId(id));

        IntStream.range(0, 10).forEach(x -> {
            try {
                supplier.get();
            } catch (Exception ignore) {
            }
        });

        //Then
        var metrics = circuitBreaker.getMetrics();
        assertThat(metrics.getNumberOfFailedCalls()).isEqualTo(5);
        assertThat(metrics.getNumberOfNotPermittedCalls()).isEqualTo(5);

        verify(middlewareApplicationServiceApi, times(5)).getApplicationSourceAccountByExternalApplicationId(id);
    }


    @Test
    void shouldOpenCircuitBreakerWhenRetryableExceptionTriggersFallback() {
        //Given
        var id = UUID.randomUUID().toString();
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(id)).thenThrow(new feign.RetryableException(-1, "", Request.HttpMethod.GET, new Date(), createRequest()));

        var supplier = circuitBreaker.decorateSupplier(() -> middlewareRepository.getApplicationByExternalApplicationId(id));

        IntStream.range(0, 10).forEach(x -> {
            try {
                supplier.get();
            } catch (Exception ignore) {
            }
        });

        //Then
        var metrics = circuitBreaker.getMetrics();
        assertThat(metrics.getNumberOfFailedCalls()).isEqualTo(5);
        assertThat(metrics.getNumberOfNotPermittedCalls()).isEqualTo(5);

        verify(middlewareApplicationServiceApi, times(5)).getApplicationSourceAccountByExternalApplicationId(id);
    }

    @Test
    void shouldNotTriggerCircuitBreakerFallbackForIgnoredExceptions() {
        //Given
        var id = UUID.randomUUID().toString();
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(id)).thenThrow(
                new FeignException.NotFound("Not found", createRequest(), "not found".getBytes(), null));

        var supplier = circuitBreaker.decorateSupplier(() -> middlewareRepository.getApplicationByExternalApplicationId(id));

        IntStream.range(0, 10).forEach(x -> {
            try {
                supplier.get();
            } catch (Exception ignore) {
            }
        });

        //Then
        var metrics = circuitBreaker.getMetrics();
        assertThat(metrics.getNumberOfFailedCalls()).isZero();
        assertThat(metrics.getNumberOfNotPermittedCalls()).isZero();

        verify(middlewareApplicationServiceApi, times(10)).getApplicationSourceAccountByExternalApplicationId(id);
    }

    private Request createRequest() {
        return Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate());
    }

    private CircuitBreaker getCircuitBreaker() {
        var config = CircuitBreakerConfig.custom()
                .failureRateThreshold(60)
                .ignoreExceptions(FeignException.FeignClientException.class)
                .recordExceptions(FeignException.FeignServerException.class, feign.RetryableException.class)
                .slidingWindowSize(5)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .build();
        var registry = CircuitBreakerRegistry.of(config);
        return registry.circuitBreaker("mw-cb");
    }
}