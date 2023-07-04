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

import com.selina.lending.internal.api.MiddlewareApplicationServiceApi;
import com.selina.lending.internal.circuitbreaker.RecordExceptionPredicate;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MiddlewareApplicationServiceRepositoryTest extends MapperBase {
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";

    @Mock
    private ApplicationIdentifier applicationIdentifier;

    @Mock
    private MiddlewareApplicationServiceApi middlewareApplicationServiceApi;

    private MiddlewareApplicationServiceRepository middlewareRepository;

    @BeforeEach
    void setUp() {
        middlewareRepository = new MiddlewareApplicationServiceRepositoryImpl(middlewareApplicationServiceApi);
    }

    @Test
    void shouldCallHttpClientWhenGetApplicationIdByExternalApplicationIdInvoked() {
        //Given
        when(middlewareApplicationServiceApi.getApplicationIdByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);

        //When
        middlewareRepository.getAppIdByExternalId(EXTERNAL_APPLICATION_ID);

        //Then
        verify(middlewareApplicationServiceApi, times(1)).getApplicationIdByExternalApplicationId(
                EXTERNAL_APPLICATION_ID);
    }

    @Test
    void shouldThrowFeignServerExceptionWhenMiddlewareThrowsInternalServerException() {
        //Given
        String errorMsg = "error";

        //When
        when(middlewareApplicationServiceApi.getApplicationIdByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenThrow(
                new FeignException.FeignServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg,
                        createRequest(), errorMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignServerException.class,
                () -> middlewareRepository.getAppIdByExternalId(EXTERNAL_APPLICATION_ID));

        //Then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
    }

    @Test
    void shouldNotTriggerCircuitBreakerFallbackForIgnoredExceptions() {
        //Given
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApplicationServiceApi.getApplicationIdByExternalApplicationId(
                EXTERNAL_APPLICATION_ID)).thenThrow(
                new FeignException.NotFound("Not found", createRequest(), "not found".getBytes(), null));

        var supplier = circuitBreaker.decorateSupplier(
                () -> middlewareRepository.getAppIdByExternalId(EXTERNAL_APPLICATION_ID));

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

        verify(middlewareApplicationServiceApi, times(10)).getApplicationIdByExternalApplicationId(
                EXTERNAL_APPLICATION_ID);
    }

    private Request createRequest() {
        return Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate());
    }

    private CircuitBreaker getCircuitBreaker() {
        var config = CircuitBreakerConfig.custom().failureRateThreshold(60).recordException(new RecordExceptionPredicate()).slidingWindowSize(5).slidingWindowType(
                CircuitBreakerConfig.SlidingWindowType.COUNT_BASED).build();
        var registry = CircuitBreakerRegistry.of(config);
        return registry.circuitBreaker("mw-cb");
    }
}