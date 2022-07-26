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

package com.selina.lending.api.interceptor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class BrokerRequestKpiFilterTest {

    @Mock
    private BrokerRequestResolver resolver;

    @InjectMocks
    private BrokerRequestKpiFilter filter;

    @Test
    void shouldNotCallBrokerHandlerWhenNotApplicatonPath() throws Exception {
        // Given
        var request = new MockHttpServletRequest();
        request.setRequestURI("/not-app-path");
        request.setMethod("POST");
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verifyNoInteractions(resolver);
    }

    @Test
    void shouldCallBrokerHandlerWhenPOSTApplicaton() throws Exception {
        // Given
        var request = new MockHttpServletRequest();
        request.setRequestURI("/application");
        request.setMethod("POST");
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(resolver, times(1)).handle(any(), any(), any());
    }

    @Test
    void shouldCallBrokerHandlerWhenPUTApplicaton() throws Exception {
        // Given
        var request = new MockHttpServletRequest();
        request.setRequestURI("/application");
        request.setMethod("PUT");
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(resolver, times(1)).handle(any(), any(), any());
    }

    @Test
    void shouldCallBrokerHandlerWhenGETApplicaton() throws Exception {
        // Given
        var request = new MockHttpServletRequest();
        request.setRequestURI("/application");
        request.setMethod("GET");
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verifyNoInteractions(resolver);
    }
}