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


import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MiddlewareRepositoryTest {

    @Mock
    private MiddlewareApi middlewareApi;

    private MiddlewareRepository middlewareRepository;

    @BeforeEach
    public void setUp() {
        middlewareRepository = new MiddlewareRepositoryImpl(middlewareApi);
    }

    @Test
    public void shouldCallHttpClientWhenGetApplicationByIdInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        var apiResponse = new ApplicationDecisionResponse();

        when(middlewareApi.getApplicationById(id)).thenReturn(apiResponse);

        // When
        var result = middlewareRepository.getApplicationById(id);

        // Then
        assertThat(result).isEqualTo(apiResponse);
        verify(middlewareApi, times(1)).getApplicationById(eq(id));
    }
}