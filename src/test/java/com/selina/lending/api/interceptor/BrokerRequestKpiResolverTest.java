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
import com.selina.lending.messaging.event.BrokerRequestFinishedEvent;
import com.selina.lending.messaging.publisher.mapper.BrokerRequestEventMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;

import static com.selina.lending.testHelper.BrokerRequestEventTestHelper.buildBrokerRequestFinishedEvent;
import static com.selina.lending.testHelper.BrokerRequestEventTestHelper.buildBrokerRequestStartedEvent;
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
    void shouldPublishMessageWhenOnRequestStartedInvoked() {
        // Given
        var broker = "the-broker";
        var requestId = UUID.randomUUID().toString();
        var httpRequest = mock(HttpServletRequest.class);

        var event = buildBrokerRequestStartedEvent();
        when(eventMapper.toStartedEvent(broker, requestId, httpRequest)).thenReturn(event);

        // When
        kpiResolver.onRequestStarted(broker, requestId, httpRequest);

        // Then
        verify(eventPublisher).publish(event);
    }

    @Test
    void shouldPublishMessageWhenOnRequestFinishedInvoked() {
        // Given
        var requestId = UUID.randomUUID().toString();
        var response = new MockHttpServletResponse();
        response.setStatus(200);

        var event = buildBrokerRequestFinishedEvent();
        when(eventMapper.toFinishedEvent(requestId, response)).thenReturn(event);

        // When
        kpiResolver.onRequestFinished(requestId, response);

        // Then
        verify(eventPublisher).publish(event);
    }

}