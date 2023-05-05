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

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.quote.middleware.QuickQuoteRequest;
import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest
class MiddlewareRepositoryFallbackTest {

    @MockBean
    private ApplicationRequest applicationRequest;

    @MockBean
    private QuickQuoteCFRequest quickQuoteCFRequest;

    @MockBean
    private QuickQuoteRequest quickQuoteRequest;

    @MockBean
    private MiddlewareApi middlewareApi;

    @Autowired
    private MiddlewareRepositoryImpl middlewareRepository;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        circuitBreaker = circuitBreakerRegistry.find("middleware-api-cb").get();
        circuitBreaker.transitionToClosedState();
    }

    @Nested
    class GetApplicationException {

        @Test
        void whenCircuitBreakerIsOpenThenThrowRemoteResourceProblemExceptionWithoutCallingMiddleware() {
            // Given
            circuitBreaker.transitionToOpenState();

            // When
            RemoteResourceProblemException requestException = assertThrows(RemoteResourceProblemException.class,
                    () -> middlewareRepository.getApplicationById("12"));

            // Then
            assertThat(requestException, isA(RemoteResourceProblemException.class));
            verifyNoInteractions(middlewareApi);
        }
    }

    @Nested
    class SelectProductException {

        @Test
        void whenCircuitBreakerIsOpenThenThrowRemoteResourceProblemExceptionWithoutCallingMiddleware() {
            // Given
            circuitBreaker.transitionToOpenState();

            // When
            RemoteResourceProblemException requestException = assertThrows(RemoteResourceProblemException.class,
                    () -> middlewareRepository.selectProduct("12", "PR01"));

            // Then
            assertThat(requestException, isA(RemoteResourceProblemException.class));
            verifyNoInteractions(middlewareApi);
        }
    }

    @Nested
    class CreateDipApplicationException {

        @Test
        void whenCircuitBreakerIsOpenThenThrowRemoteResourceProblemExceptionWithoutCallingMiddleware() {
            // Given
            circuitBreaker.transitionToOpenState();

            // When
            RemoteResourceProblemException requestException = assertThrows(RemoteResourceProblemException.class,
                    () -> middlewareRepository.createDipApplication(applicationRequest));

            // Then
            assertThat(requestException, isA(RemoteResourceProblemException.class));
            verifyNoInteractions(middlewareApi);
        }
    }

    @Nested
    class CreateDipCCApplicationException {

        @Test
        void whenCircuitBreakerIsOpenThenThrowRemoteResourceProblemExceptionWithoutCallingMiddleware() {
            // Given
            circuitBreaker.transitionToOpenState();

            // When
            RemoteResourceProblemException requestException = assertThrows(RemoteResourceProblemException.class,
                    () -> middlewareRepository.createDipCCApplication(applicationRequest));

            // Then
            assertThat(requestException, isA(RemoteResourceProblemException.class));
            verifyNoInteractions(middlewareApi);
        }
    }

    @Nested
    class CreateQuickQuoteCFApplicationException {

        @Test
        void whenCircuitBreakerIsOpenThenThrowRemoteResourceProblemExceptionWithoutCallingMiddleware() {
            // Given
            circuitBreaker.transitionToOpenState();

            // When
            RemoteResourceProblemException requestException = assertThrows(RemoteResourceProblemException.class,
                    () -> middlewareRepository.createQuickQuoteCFApplication(quickQuoteCFRequest));

            // Then
            assertThat(requestException, isA(RemoteResourceProblemException.class));
            verifyNoInteractions(middlewareApi);
        }
    }

    @Nested
    class CreateQuickQuoteAggregatorApplicationException {

        @Test
        void whenCircuitBreakerIsOpenThenThrowRemoteResourceProblemExceptionWithoutCallingMiddleware() {
            // Given
            circuitBreaker.transitionToOpenState();

            // When
            RemoteResourceProblemException requestException = assertThrows(RemoteResourceProblemException.class,
                    () -> middlewareRepository.createQuickQuoteApplication(quickQuoteRequest));

            // Then
            assertThat(requestException, isA(RemoteResourceProblemException.class));
            verifyNoInteractions(middlewareApi);
        }
    }
}
