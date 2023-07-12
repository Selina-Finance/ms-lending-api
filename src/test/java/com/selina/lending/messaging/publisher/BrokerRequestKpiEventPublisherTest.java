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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.SettableListenableFuture;

import static com.selina.lending.testutil.BrokerRequestKpiEventTestHelper.buildBrokerRequestKpiEvent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerRequestKpiEventPublisherTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private BrokerRequestKpiEventPublisher publisher;

    @Test
    void shouldInvokeKafkaTemplateWithCorrectArgumentsWhenPublishStartedEvent() {
        // Given
        var event = buildBrokerRequestKpiEvent();

        var injectedFromConfigTopicName = "the-broker-topic-name";
        ReflectionTestUtils.setField(publisher, "brokerRequestKpiTopicName", injectedFromConfigTopicName);

        SettableListenableFuture<SendResult<String, Object>> future = new SettableListenableFuture<>();
        when(kafkaTemplate.send(any(), any(), any())).thenReturn(future);

        // When
        publisher.publish(event);

        // Then
        verify(kafkaTemplate, times(1)).send(injectedFromConfigTopicName, event.source(), event);
    }

}