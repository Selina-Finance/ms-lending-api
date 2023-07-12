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

package com.selina.lending.service;

import com.selina.lending.messaging.mapper.BrokerRequestEventMapper;
import com.selina.lending.messaging.publisher.BrokerRequestEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.time.Instant;
import java.util.Optional;

import static com.selina.lending.testutil.BrokerRequestEventTestHelper.buildBrokerRequestKpiEvent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerRequestKpiServiceTest {

    @Mock
    private BrokerRequestEventPublisher publisher;
    @Mock
    private BrokerRequestEventMapper mapper;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private BrokerRequestKpiService resolver;

    @Test
    void shouldDoNotPublishEventWheMapperReturnedEmpty() {
        // Given
        var request = new MockHttpServletRequest();

        when(mapper.toEvent(any(), any(), any(), any())).thenReturn(Optional.empty());

        // When
        resolver.handle(new ContentCachingRequestWrapper(request), null, null);

        // Then
        verifyNoInteractions(publisher);
    }

    @Test
    void shouldPublishEventWheMapperReturnedResult() {
        // Given
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var started = Instant.now();
        var event = buildBrokerRequestKpiEvent();
        when(mapper.toEvent(any(), any(), any(), any())).thenReturn(Optional.of(event));

        // When
        resolver.handle(
                new ContentCachingRequestWrapper(request),
                new ContentCachingResponseWrapper(response),
                started
        );

        // Then
        verify(publisher, times(1)).publish(event);
    }

}