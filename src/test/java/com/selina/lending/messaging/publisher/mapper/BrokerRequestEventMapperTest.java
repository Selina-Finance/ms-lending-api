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

package com.selina.lending.messaging.publisher.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.mapper.MapperBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerRequestEventMapperTest extends MapperBase {
    private static final String REQUEST_ID_HEADER_NAME = "x-selina-request-id";
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BrokerRequestEventMapper mapper;

    @Test
    void shouldMapToBrokerRequestKpiEvent() throws IOException {
        // Given
        var clientId = "the-broker-id";
        var started = Instant.now();
        var foreignRequestId = "123";
        var uriPath = "/application/123";
        var httpMethod = "POST";
        var ip = "192.0.0.1";

        var httpRequest = new MockHttpServletRequest();
        httpRequest.setRequestURI(uriPath);
        httpRequest.setMethod(httpMethod);
        httpRequest.setRemoteAddr(ip);
        httpRequest.addHeader(REQUEST_ID_HEADER_NAME, foreignRequestId);

        var httpResponseCode = 200;
        var response = new MockHttpServletResponse();
        response.setStatus(httpResponseCode);

        ApplicationResponse appResponse = ApplicationResponse.builder().application(getDIPApplicationDto()).build();
        when(objectMapper.readValue(new byte[]{}, ApplicationResponse.class)).thenReturn(appResponse);

        // When
        var optResult = mapper.toBrokerRequestKpiEvent(
                new ContentCachingRequestWrapper(httpRequest),
                new ContentCachingResponseWrapper(response),
                started,
                clientId
        );

        // Then
        assertTrue(optResult.isPresent());
        var result = optResult.get();

        assertThat(result.requestId()).isEqualTo(foreignRequestId);
        assertThat(result.externalApplicationId()).isEqualTo(appResponse.getApplication().getExternalApplicationId());
        assertThat(result.ip()).isEqualTo(ip);
        assertThat(result.source()).isEqualTo(clientId);
        assertThat(result.uriPath()).isEqualTo(uriPath);
        assertThat(result.httpMethod()).isEqualTo(httpMethod);
        assertThat(result.httpResponseCode()).isEqualTo(httpResponseCode);
        assertThat(result.decision()).isEqualTo(appResponse.getApplication().getStatus());
        assertThat(result.started()).isEqualTo(started);
        assertThat(result.finished()).isAfter(started);
    }

    @Test
    void shouldGenerateRequestIdWhenHeaderIsEmpty() throws IOException {
        // Given
        var httpRequest = new MockHttpServletRequest(); // without request-id header
        var response = new MockHttpServletResponse();

        ApplicationResponse appResponse = ApplicationResponse.builder().application(getDIPApplicationDto()).build();
        when(objectMapper.readValue(new byte[]{}, ApplicationResponse.class)).thenReturn(appResponse);

        // When
        var optResult = mapper.toBrokerRequestKpiEvent(
                new ContentCachingRequestWrapper(httpRequest),
                new ContentCachingResponseWrapper(response),
                Instant.now(),
                "the-broker-id"
        );

        // Then
        assertTrue(optResult.isPresent());
        var result = optResult.get();
        assertThat(result.requestId()).isNotNull();
    }

    @Test
    void shouldReturnEmptyWhenCanNotProperlyReadResponse() throws IOException {
        // Given
        var httpRequest = new MockHttpServletRequest(); // without request-id header
        var response = new MockHttpServletResponse();

        when(objectMapper.readValue(new byte[]{}, ApplicationResponse.class)).thenThrow(new IOException("error"));

        // When
        var optResult = mapper.toBrokerRequestKpiEvent(
                new ContentCachingRequestWrapper(httpRequest),
                new ContentCachingResponseWrapper(response),
                Instant.now(),
                "the-broker-id"
        );

        // Then
        assertTrue(optResult.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenApplicationNotInResponse() throws IOException {
        // Given
        var httpRequest = new MockHttpServletRequest(); // without request-id header
        var response = new MockHttpServletResponse();

        ApplicationResponse appResponse = ApplicationResponse.builder().build();
        when(objectMapper.readValue(new byte[]{}, ApplicationResponse.class)).thenReturn(appResponse);

        // When
        var optResult = mapper.toBrokerRequestKpiEvent(
                new ContentCachingRequestWrapper(httpRequest),
                new ContentCachingResponseWrapper(response),
                Instant.now(),
                "the-broker-id"
        );

        // Then
        assertTrue(optResult.isEmpty());
    }
}