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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class MiddlewareRepositoryFallbackTests {

    @Mock
    private FeignException.FeignServerException feignServerException;

    @Mock
    private feign.RetryableException retryableException;

    @InjectMocks
    private MiddlewareRepositoryImpl middlewareRepository;

    @Nested
    class GetApplicationExceptions {
        @Test
        void shouldInvokeMiddlewareApiFallbackForFeignServerException() throws NoSuchMethodException {
            //Given
            Method fallback = MiddlewareRepositoryImpl.class.getDeclaredMethod("middlewareGetApiFallback",
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
            Method fallback = MiddlewareRepositoryImpl.class.getDeclaredMethod("middlewareGetApiFallback",
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
    class CreateApplicationExceptions {
        @Test
        void shouldInvokeMiddlewareApiFallbackForFeignServerException() throws NoSuchMethodException {
            //Given
            Method fallback = MiddlewareRepositoryImpl.class.getDeclaredMethod("middlewareApiFallback",
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
            Method fallback = MiddlewareRepositoryImpl.class.getDeclaredMethod("middlewareApiFallback",
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
    class SelectProductExceptions {

        @Test
        void shouldInvokeMiddlewareApiFallbackForFeignServerException() throws NoSuchMethodException {
            //Given
            Method fallback = MiddlewareRepositoryImpl.class.getDeclaredMethod("middlewareApiSelectProductFallback",
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
            Method fallback = MiddlewareRepositoryImpl.class.getDeclaredMethod("middlewareApiSelectProductFallback",
                    feign.RetryableException.class);
            fallback.setAccessible(true);

            //When
            Exception exception = assertThrows(InvocationTargetException.class,
                    () -> fallback.invoke(middlewareRepository, retryableException));

            //Then
            assertEquals(RemoteResourceProblemException.class, exception.getCause().getClass());
        }
    }
}
