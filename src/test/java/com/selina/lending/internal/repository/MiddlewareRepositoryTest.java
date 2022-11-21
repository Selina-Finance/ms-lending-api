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

import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.dto.Source;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.Application;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.internal.service.application.domain.CreditCommitmentResponse;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@ExtendWith(MockitoExtension.class)
class MiddlewareRepositoryTest {
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";

    @Mock
    private MiddlewareApi middlewareApi;

    @Mock
    private ApplicationRequest applicationRequest;

    @Mock
    private ApplicationResponse applicationResponse;

    @Mock
    private Application application;

    @Mock
    private CreditCommitmentResponse creditCommitmentResponse;

    private MiddlewareRepository middlewareRepository;

    @Mock
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        middlewareRepository = new MiddlewareRepositoryImpl(middlewareApi, tokenService);
    }

    @Test
    void shouldCallHttpClientWhenGetApplicationByIdInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        var apiResponse = ApplicationDecisionResponse.builder().build();
        when(middlewareApi.getApplicationById(id)).thenReturn(apiResponse);

        // When
        var result = middlewareRepository.getApplicationById(id);

        // Then
        assertThat(result).isEqualTo(Optional.of(apiResponse));
        verify(middlewareApi, times(1)).getApplicationById(id);
    }

    @Test
    void shouldCallHttpClientWhenCreateApplicationInvoked() {
        // Given
        when(middlewareApi.createDipApplication(applicationRequest)).thenReturn(applicationResponse);
        when(applicationResponse.getApplication()).thenReturn(application);
        when(application.getExternalApplicationId()).thenReturn(EXTERNAL_APPLICATION_ID);

        // When
        var result = middlewareRepository.createDipApplication(applicationRequest);

        // Then
        assertThat(result).isEqualTo(applicationResponse);
        verify(applicationRequest, times(1)).setSource(Source.BROKER.toString());
        verify(middlewareApi, times(1)).createDipApplication(applicationRequest);
    }

    @Test
    void shouldCallHttpClientWhenGetCreditCommitmentsInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        when(middlewareApi.getCreditCommitmentByApplicationId(id)).thenReturn(creditCommitmentResponse);

        // When
        var result = middlewareRepository.getCreditCommitments(id);

        // Then
        assertThat(result).isEqualTo(creditCommitmentResponse);
        verify(middlewareApi, times(1)).getCreditCommitmentByApplicationId(id);
    }

    @Test
    void shouldThrowFeignServerExceptionWhenMiddlewareThrowsInternalServerException() {
        //Given
        String errorMsg = "error";
        var id = UUID.randomUUID().toString();

        //When
        when(middlewareApi.getApplicationById(id)).thenThrow(
                new FeignException.FeignServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg, createRequest(),
                        errorMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignServerException.class,
                () -> middlewareRepository.getApplicationById(id));

        //Then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
    }

    @Test
    void shouldThrowFeignClientExceptionWhenMiddlewareThrowsNotFoundException() {
        //Given
        String notFoundMsg = "not found";

        //When
        when(middlewareApi.createDipApplication(applicationRequest)).thenThrow(
                new FeignException.FeignClientException(HttpStatus.NOT_FOUND.value(), notFoundMsg, createRequest(),
                        notFoundMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignClientException.class,
                () -> middlewareRepository.createDipApplication(applicationRequest));

        //Then
        assertThat(exception.getMessage()).isEqualTo(notFoundMsg);
    }

    @Test
    void shouldThrowFeignClientExceptionWhenMiddlewareGetCreditCommitmentsThrowsNotFoundException() {
        //Given
        String notFoundMsg = "not found";
        var id = UUID.randomUUID().toString();

        //When
        when(middlewareApi.getCreditCommitmentByApplicationId(id)).thenThrow(
                new FeignException.FeignClientException(HttpStatus.NOT_FOUND.value(), notFoundMsg, createRequest(),
                        notFoundMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignClientException.class,
                () -> middlewareRepository.getCreditCommitments(id));

        //Then
        assertThat(exception.getMessage()).isEqualTo(notFoundMsg);
    }

    @Test
    void shouldThrowFeignServerExceptionWhenMiddlewareGetCreditCommitmentsThrowsInternalServerException() {
        //Given
        String errorMsg = "error";
        var id = UUID.randomUUID().toString();

        //When
        when(middlewareApi.getCreditCommitmentByApplicationId(id)).thenThrow(
                new FeignException.FeignServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg, createRequest(),
                        errorMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignServerException.class,
                () -> middlewareRepository.getCreditCommitments(id));

        //Then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
    }

    @Test
    void shouldOpenCircuitBreakerWhenFeignServerExceptionTriggersFallback() {
        //Given
        var id = UUID.randomUUID().toString();
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApi.getApplicationById(id)).thenThrow(
                new FeignException.InternalServerError("Internal Server Error", createRequest(), "error".getBytes(), null));

        var supplier = circuitBreaker.decorateSupplier(() -> middlewareRepository.getApplicationById(id));

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

        verify(middlewareApi, times(5)).getApplicationById(id);
    }

    @Test
    void shouldOpenCircuitBreakerWhenRetryableExceptionTriggersFallback() {
        //Given
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApi.createDipApplication(applicationRequest)).thenThrow(new feign.RetryableException(-1, "", Request.HttpMethod.GET, new Date(), createRequest()));

        var supplier = circuitBreaker.decorateSupplier(() -> middlewareRepository.createDipApplication(applicationRequest));

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

        verify(middlewareApi, times(5)).createDipApplication(applicationRequest);
    }

    @Test
    void shouldNotTriggerCircuitBreakerFallbackForIgnoredExceptions() {
        //Given
        var id = UUID.randomUUID().toString();
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApi.getApplicationById(id)).thenThrow(
                new FeignException.NotFound("Not found", createRequest(), "not found".getBytes(), null));

        var supplier = circuitBreaker.decorateSupplier(() -> middlewareRepository.getApplicationById(id));

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

        verify(middlewareApi, times(10)).getApplicationById(id);
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