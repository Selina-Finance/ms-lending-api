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

import com.selina.lending.messaging.event.BrokerRequestEvent;
import com.selina.lending.messaging.event.KafkaEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "kafka.enable", havingValue = "true", matchIfMissing = true)
public class BrokerRequestEventPublisher {

    @Value(value = "${kafka.topics.brokerRequestKpi.name}")
    private String brokerRequestKpiTopicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BrokerRequestEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(BrokerRequestEvent event) {
        log.debug("Request to publish BrokerRequestKpiEvent: {}", event);
        kafkaTemplate.send(brokerRequestKpiTopicName, event.key(), event).addCallback(
                result -> successfulCallback(event, result),
                ex -> errorCallback(event, ex)
        );
    }

    private void successfulCallback(KafkaEvent payload, @NotNull SendResult<String, Object> result) {
        log.debug("Sent event:{} with offset:{}", payload, result.getRecordMetadata().offset());
    }

    private void errorCallback(KafkaEvent payload, @NotNull Throwable ex) {
        log.error("Unable to send event: {} due to: {}", payload, ex.getMessage());
    }

}
