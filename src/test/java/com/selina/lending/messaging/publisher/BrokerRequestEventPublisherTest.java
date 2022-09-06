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

package com.selina.lending.messaging.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.messaging.kafka.KafkaManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.selina.lending.testHelper.BrokerRequestEventTestHelper.buildBrokerRequestStartedEvent;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerRequestEventPublisherTest {

    @Mock
    private ObjectMapper mapper;
    @Mock
    private KafkaManager kafkaManager;

    @InjectMocks
    private BrokerRequestEventPublisher publisher;

    @Test
    public void shouldInvokeKafkaManagerWithCorrectArguments() throws JsonProcessingException {
        // Given
        var event = buildBrokerRequestStartedEvent();

        var eventAsJsonString = "this-would-be-event-as-json-string";
        when(mapper.writeValueAsString(any())).thenReturn(eventAsJsonString);
        Mockito.doNothing().when(kafkaManager).publish(any(), any(), any());

        // When
        publisher.publish(event);

        // Then
        verify(kafkaManager, times(1)).publish("private.ms-lending-api.broker-request.local", event.requestId(), eventAsJsonString);
    }

    @Test
    public void shouldNotInvokeKafkaManagerWhenEventIsNotSerializableToJsonString() throws JsonProcessingException {
        // Given
        var event = buildBrokerRequestStartedEvent();
        when(mapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        // When
        assertThatThrownBy(() -> publisher.publish(event)).isInstanceOf(RuntimeException.class);

        // Then
        verifyNoInteractions(kafkaManager);
    }

}