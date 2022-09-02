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

package com.selina.lending.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String topic, String key, String payload) {
        log.debug("Publishing event. Topic: {}, Key: {}, Payload: {}", topic, key, payload);
        kafkaTemplate.send(topic, key, payload)
                .addCallback(
                        result -> successfulCallback(payload, result),
                        ex -> errorCallback(payload, ex)
                );
    }

    private void successfulCallback(String payload, SendResult<String, String> result) {
        log.debug("Sent event:{} with offset:{}", payload, result.getRecordMetadata().offset());
    }

    private void errorCallback(String payload, Throwable ex) {
        log.error("Unable to send event: {} due to: {}", payload, ex.getMessage());
    }
}
