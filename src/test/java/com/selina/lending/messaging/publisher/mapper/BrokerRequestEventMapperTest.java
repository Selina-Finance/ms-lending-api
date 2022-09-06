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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerRequestEventMapperTest {

    @InjectMocks
    private BrokerRequestEventMapper mapper;

    @Test
    public void shouldMapToBrokerRequestFinishedEvent() {
        // Given
        var requestId = "123";
        var httpResponseCode = 502;

        // When
        var result = mapper.toFinishedEvent(requestId, httpResponseCode);

        // Then
        assertThat(result.requestId()).isEqualTo(requestId);
        assertThat(result.httpResponseCode()).isEqualTo(httpResponseCode);
    }

    @Test
    public void shouldMapToBrokerRequestStartedEvent() {
        // Given
        var broker = "the-broker";
        var requestId = "123";
        var path = "/abra/cadabra";
        var ip = "192.0.0.1";

        var httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getRequestURI()).thenReturn(path);
        when(httpRequest.getRemoteAddr()).thenReturn(ip);

        // When
        var result = mapper.toStartedEvent(broker, requestId, httpRequest);

        // Then
        assertThat(result.requestId()).isEqualTo(requestId);
        assertThat(result.broker()).isEqualTo(broker);
        assertThat(result.created()).isBeforeOrEqualTo(Instant.now());

        assertThat(result.path()).isEqualTo(path);
        assertThat(result.ip()).isEqualTo(ip);
    }

//    String path,
//    String ip
}