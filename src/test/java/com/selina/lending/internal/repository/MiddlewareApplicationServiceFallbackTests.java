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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.service.monitoring.MetricService;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class MiddlewareApplicationServiceFallbackTests {

    @Mock
    private FeignException.FeignServerException feignServerException;

    @Mock
    private feign.RetryableException retryableException;

    @Mock
    private MetricService metricService;

    @InjectMocks
    private MiddlewareApplicationServiceRepositoryImpl middlewareRepository;

    @Nested
    class GetApplicationExceptions {
        @Test
        void shouldInvokeMiddlewareApiFallbackForFeignServerException() throws NoSuchMethodException {
            //Given
            Method fallback = MiddlewareApplicationServiceRepositoryImpl.class.getDeclaredMethod("middlewareGetByExternalIdApiFallback",
                    FeignException.FeignServerException.class);
            fallback.setAccessible(true);

            //When
            Exception exception = assertThrows(InvocationTargetException.class,
                    () -> fallback.invoke(middlewareRepository, feignServerException));

            //Then
            assertEquals(RemoteResourceProblemException.class, exception.getCause().getClass());
        }

        @Test
        void shouldInvokeMiddlewareApiFallbackForRetryableException() throws NoSuchMethodException {
            //Given
            Method fallback = MiddlewareApplicationServiceRepositoryImpl.class.getDeclaredMethod("middlewareGetByExternalIdApiFallback",
                    feign.RetryableException.class);
            fallback.setAccessible(true);

            //When
            Exception exception = assertThrows(InvocationTargetException.class,
                    () -> fallback.invoke(middlewareRepository, retryableException));

            //Then
            assertEquals(RemoteResourceProblemException.class, exception.getCause().getClass());
        }
    }

    @Nested
    class DeleteApplicationExceptions {
        @Test
        void shouldInvokeMiddlewareApiFallbackForFeignServerException()
                throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            //Given
            Method fallback = MiddlewareApplicationServiceRepositoryImpl.class.getDeclaredMethod("deleteApiFallback",
                    String.class, String.class, FeignException.FeignServerException.class);
            fallback.setAccessible(true);

            //When
            fallback.invoke(middlewareRepository, "any", "any", feignServerException);

            //Then
            verify(metricService, times(1)).incrementApplicationDeleteFailed();
        }

        @Test
        void shouldInvokeMiddlewareApiFallbackForRetryableException()
                throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            //Given
            Method fallback = MiddlewareApplicationServiceRepositoryImpl.class.getDeclaredMethod("deleteApiFallback",
                    String.class, String.class, feign.RetryableException.class);
            fallback.setAccessible(true);

            //When
            fallback.invoke(middlewareRepository, "any", "any", retryableException);

            //Then
            verify(metricService, times(1)).incrementApplicationDeleteFailed();
        }
    }
}
