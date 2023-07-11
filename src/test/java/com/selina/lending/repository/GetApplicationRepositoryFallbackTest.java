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

package com.selina.lending.repository;

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.httpclient.getapplication.GetApplicationApi;
import com.selina.lending.repository.GetApplicationRepositoryImpl;
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
class GetApplicationRepositoryFallbackTest {

    @MockBean
    private GetApplicationApi middlewareApi;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker circuitBreaker;

    @Autowired
    private GetApplicationRepositoryImpl middlewareRepository;

    @BeforeEach
    void setUp() {
        circuitBreaker = circuitBreakerRegistry.find("middleware-application-service-cb").get();
        circuitBreaker.transitionToClosedState();
    }

    @Nested
    class GetApplicationByIdExceptions {
        @Test
        void whenCircuitBreakerIsOpenThenThrowRemoteResourceProblemExceptionWithoutCallingMiddleware() {
            // Given
            circuitBreaker.transitionToOpenState();

            // When
            RemoteResourceProblemException requestException = assertThrows(RemoteResourceProblemException.class,
                    () -> middlewareRepository.getAppIdByExternalId("id"));

            // Then
            assertThat(requestException, isA(RemoteResourceProblemException.class));
            verifyNoInteractions(middlewareApi);
        }
    }
}
