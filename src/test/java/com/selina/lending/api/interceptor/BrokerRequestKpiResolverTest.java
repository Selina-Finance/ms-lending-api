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

import com.selina.lending.messaging.publisher.BrokerRequestEventPublisher;
import com.selina.lending.messaging.publisher.event.BrokerRequestFinishedEvent;
import com.selina.lending.messaging.publisher.event.BrokerRequestStartedEvent;
import com.selina.lending.messaging.publisher.mapper.BrokerRequestEventMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerRequestKpiResolverTest {

    @Mock
    private BrokerRequestEventPublisher eventPublisher;
    @Mock
    private BrokerRequestEventMapper eventMapper;

    @InjectMocks
    private BrokerRequestKpiResolver kpiResolver;

    @Test
    public void shouldPublishMessageWhenOnRequestStartedInvoked() {
        // Given
        var broker = "the-broker";
        var requestId = UUID.randomUUID().toString();
        var httpRequest = mock(HttpServletRequest.class);

        var event = new BrokerRequestStartedEvent("", Instant.now(), "", "", "");
        when(eventMapper.toStartedEvent(broker, requestId, httpRequest)).thenReturn(event);

        // When
        kpiResolver.onRequestStarted(broker, requestId, httpRequest);

        // Then
        verify(eventPublisher).publish(eq(event));
    }

    @Test
    public void shouldPublishMessageWhenOnRequestFinishedInvoked() {
        // Given
        var requestId = UUID.randomUUID().toString();
        var httpResponseCode = 200;


        var event = new BrokerRequestFinishedEvent("", httpResponseCode);
        when(eventMapper.toFinishedEvent(requestId, httpResponseCode)).thenReturn(event);

        // When
        kpiResolver.onRequestFinished(requestId, httpResponseCode);

        // Then
        verify(eventPublisher).publish(eq(event));
    }

}