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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerRequestInterceptorTest {

    @Mock
    private BrokerRequestKpiResolver kpiResolver;

    @InjectMocks
    private BrokerRequestInterceptor interceptor;

    private static final String BROKER_JWT_CLIENT_ID = "fake-client-id";
    private static final String BROKER_HTTP_ATTR_NAME = "broker-request-id";

    @Test
    public void shouldSetBrokerRequestIdToHttpRequestAttributesWhenPreHandle() throws Exception {
        // Given
        var httpRequest = mock(HttpServletRequest.class);
        doNothing().when(httpRequest).setAttribute(eq(BROKER_HTTP_ATTR_NAME), any());
        mockSecurity();

        // When
        interceptor.preHandle(httpRequest, null, null);

        // Then
        verify(httpRequest, times(1)).setAttribute(eq(BROKER_HTTP_ATTR_NAME), any());
    }

    @Test
    public void shouldPassRequestDataToResolverOnRequestStartedWhenExecutePreHandle() throws Exception {
        // Given
        var httpRequest = mock(HttpServletRequest.class);
        doNothing().when(kpiResolver).onRequestStarted(any(), any(), any());
        mockSecurity();

        // When
        interceptor.preHandle(httpRequest, null, null);

        // Then
        verify(kpiResolver, times(1)).onRequestStarted(eq(BROKER_JWT_CLIENT_ID), any(), eq(httpRequest));
    }

    private void mockSecurity() {
        Authentication authentication = Mockito.mock(Authentication.class);

        Jwt jwt = Jwt.withTokenValue("abc").header("", "").claim("clientId", BROKER_JWT_CLIENT_ID).build();
        when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldPassRequestIdAndResponseStatusWhenFinishingRequest() throws Exception {
        // Given
        var requestId = UUID.randomUUID().toString();
        var httpResponseCode = 200;

        var request = mock(HttpServletRequest.class);
        when(request.getAttribute(BROKER_HTTP_ATTR_NAME)).thenReturn(requestId);

        var response = mock(HttpServletResponse.class);
        when(response.getStatus()).thenReturn(httpResponseCode);

        doNothing().when(kpiResolver).onRequestFinished(any(), any());

        // When
        interceptor.afterCompletion(request, response, null, null);

        // Then
        verify(kpiResolver, times(1)).onRequestFinished(eq(requestId), eq(httpResponseCode));
    }

    @Test
    public void shouldNotCallKpiResolverOnPostHandleWhenBrokerRequestIdIsNull() throws Exception {
        // Given
        String requestId = null;
        var httpResponseCode = 200;

        var request = mock(HttpServletRequest.class);
        when(request.getAttribute(BROKER_HTTP_ATTR_NAME)).thenReturn(requestId);

        var response = mock(HttpServletResponse.class);
        when(response.getStatus()).thenReturn(httpResponseCode);

        // When
        interceptor.afterCompletion(request, response, null, null);

        // Then
        verify(kpiResolver, times(0)).onRequestFinished(eq(requestId), eq(httpResponseCode));
    }


}