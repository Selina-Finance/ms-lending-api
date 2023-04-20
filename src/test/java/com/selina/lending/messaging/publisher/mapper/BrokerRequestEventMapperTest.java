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
import com.selina.lending.internal.dto.DIPApplicationResponse;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.messaging.mapper.brokerrequest.BrokerRequestEventMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Instant;

import static org.apache.commons.codec.CharEncoding.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerRequestEventMapperTest extends MapperBase {
    private static final String REQUEST_ID_HEADER_NAME = "x-selina-request-id";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private BrokerRequestEventMapper mapper;

    @Test
    void shouldMapToBrokerRequestKpiEvent() throws IOException {
        // Given
        var clientId = "the-broker-id";
        var started = Instant.now();
        var uriPath = "/application/123";
        var httpMethod = "POST";
        var ip = "192.0.0.1";

        var request = Mockito.mock(ContentCachingRequestWrapper.class);
        when(request.getRequestURI()).thenReturn(uriPath);
        when(request.getMethod()).thenReturn(httpMethod);
        when(request.getRemoteAddr()).thenReturn(ip);
        var requestBody = getDIPApplicationRequestDto();
        var requestAsAString = objectMapper.writeValueAsString(requestBody);
        when(request.getContentAsByteArray()).thenReturn(requestAsAString.getBytes());
        when(request.getCharacterEncoding()).thenReturn(UTF_8);

        var response = Mockito.mock(ContentCachingResponseWrapper.class);
        var httpResponseCode = 200;
        when(response.getStatus()).thenReturn(httpResponseCode);
        var responseBody = DIPApplicationResponse.builder().application(getDIPApplicationDto()).build();
        var responseAsAString = objectMapper.writeValueAsString(responseBody);
        when(response.getContentAsByteArray()).thenReturn(responseAsAString.getBytes());
        when(response.getCharacterEncoding()).thenReturn(UTF_8);

        // When
        var optResult = mapper.toEvent(request, response, started, clientId);

        // Then
        assertTrue(optResult.isPresent());
        var result = optResult.get();

        assertThat(result.ip()).isEqualTo(ip);
        assertThat(result.source()).isEqualTo(clientId);
        assertThat(result.uriPath()).isEqualTo(uriPath);
        assertThat(result.httpMethod()).isEqualTo(httpMethod);
        assertThat(result.httpRequestBody()).isEqualTo(requestAsAString);
        assertThat(result.httpResponseBody()).isEqualTo(responseAsAString);
        assertThat(result.httpResponseCode()).isEqualTo(httpResponseCode);
        assertThat(result.started()).isEqualTo(started);
        assertThat(result.finished()).isAfter(started);
    }

    @Test
    void shouldReturnEmptyWhenErrorParsingRequest() {
        // Given
        var request = Mockito.mock(ContentCachingRequestWrapper.class);
        var response = Mockito.mock(ContentCachingResponseWrapper.class);

        when(request.getContentAsByteArray()).thenThrow(new IndexOutOfBoundsException("error"));

        // When
        var optResult = mapper.toEvent(request, response, Instant.now(), "the-broker-id");

        // Then
        assertTrue(optResult.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenErrorParsingResponse() {
        // Given
        var request = Mockito.mock(ContentCachingRequestWrapper.class);
        var response = Mockito.mock(ContentCachingResponseWrapper.class);

        when(response.getContentAsByteArray()).thenThrow(new IndexOutOfBoundsException("error"));

        // When
        var optResult = mapper.toEvent(request, response, Instant.now(), "the-broker-id");

        // Then
        assertTrue(optResult.isEmpty());
    }
}