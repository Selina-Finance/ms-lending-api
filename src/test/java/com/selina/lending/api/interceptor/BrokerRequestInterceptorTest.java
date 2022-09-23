///*
// * Copyright 2022 Selina Finance
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//
//package com.selina.lending.api.interceptor;
//
//import com.selina.lending.internal.service.TokenService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockHttpServletResponse;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class BrokerRequestInterceptorTest {
//
//    @Mock
//    private BrokerRequestKpiResolver kpiResolver;
//
//    @Mock
//    private TokenService tokenService;
//
//    @InjectMocks
//    private BrokerRequestInterceptor interceptor;
//
//    private static final String BROKER_JWT_CLIENT_ID = "fake-client-id";
//    private static final String BROKER_HTTP_ATTR_NAME = "broker-request-id";
//    private static final String REQUEST_ID_HEADER_NAME = "x-selina-request-id";
//
//    @Test
//    void shouldSetForeignRequestIdFromHeadersToHttpRequestAttributesWhenPreHandle() throws Exception {
//        // Given
//        var foreignRequestId = "123";
//        var httpRequest = mock(HttpServletRequest.class);
//        doNothing().when(httpRequest).setAttribute(eq(BROKER_HTTP_ATTR_NAME), any());
//        when(httpRequest.getHeader(REQUEST_ID_HEADER_NAME)).thenReturn(foreignRequestId);
//        when(tokenService.retrieveClientId()).thenReturn(BROKER_JWT_CLIENT_ID);
//
//        // When
//        interceptor.preHandle(httpRequest, null, null);
//
//        // Then
//        verify(httpRequest, times(1)).getHeader(REQUEST_ID_HEADER_NAME);
//        verify(httpRequest, times(1)).setAttribute(BROKER_HTTP_ATTR_NAME, foreignRequestId);
//    }
//
//    @Test
//    void shouldGenerateRequestIdAndSetToHttpRequestAttributesWhenNotPresentedInHeadersOnPreHandle() throws Exception {
//        // Given
//        var httpRequest = mock(HttpServletRequest.class);
//        doNothing().when(httpRequest).setAttribute(eq(BROKER_HTTP_ATTR_NAME), any());
//        when(httpRequest.getHeader(REQUEST_ID_HEADER_NAME)).thenReturn(null);
//        when(tokenService.retrieveClientId()).thenReturn(BROKER_JWT_CLIENT_ID);
//
//        // When
//        interceptor.preHandle(httpRequest, null, null);
//
//        // Then
//        verify(httpRequest, times(1)).getHeader(REQUEST_ID_HEADER_NAME);
//        verify(httpRequest, times(1)).setAttribute(eq(BROKER_HTTP_ATTR_NAME), any());
//    }
//
//    @Test
//    void shouldPassRequestDataToResolverOnRequestStartedWhenExecutePreHandle() throws Exception {
//        // Given
//        var requestId = "abc";
//        var httpRequest = mock(HttpServletRequest.class);
//        doNothing().when(kpiResolver).onRequestStarted(any(), any(), any());
//        when(httpRequest.getHeader(REQUEST_ID_HEADER_NAME)).thenReturn(requestId);
//        when(tokenService.retrieveClientId()).thenReturn(BROKER_JWT_CLIENT_ID);
//
//        // When
//        interceptor.preHandle(httpRequest, null, null);
//
//        // Then
//        verify(kpiResolver, times(1)).onRequestStarted(BROKER_JWT_CLIENT_ID, requestId, httpRequest);
//    }
//
//    @Test
//    void shouldPassRequestIdAndResponseStatusWhenFinishingRequest() throws Exception {
//        // Given
//        var requestId = UUID.randomUUID().toString();
//        var response = new MockHttpServletResponse();
//
//        var request = mock(HttpServletRequest.class);
//        when(request.getAttribute(BROKER_HTTP_ATTR_NAME)).thenReturn(requestId);
//
//        doNothing().when(kpiResolver).onRequestFinished(any(), any());
//
//        // When
//        interceptor.afterCompletion(request, response, null, null);
//
//        // Then
//        verify(kpiResolver, times(1)).onRequestFinished(requestId, response);
//    }
//
//    @Test
//    void shouldNotCallKpiResolverOnPostHandleWhenBrokerRequestIdIsNull() throws Exception {
//        // Given
//        String requestId = null;
//
//        var request = mock(HttpServletRequest.class);
//        when(request.getAttribute(BROKER_HTTP_ATTR_NAME)).thenReturn(requestId);
//
//        var response = new MockHttpServletResponse();
//
//        // When
//        interceptor.afterCompletion(request, response, null, null);
//
//        // Then
//        verify(kpiResolver, times(0)).onRequestFinished(requestId, response);
//    }
//}