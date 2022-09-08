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
import com.selina.lending.messaging.event.BrokerRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BrokerRequestEventPublisher implements EventPublisher<BrokerRequestEvent> {

    private final ObjectMapper mapper;
    private final KafkaManager kafkaManager;

    // TODO: move to remote config
    public static final String BROKER_REQUEST_TOPIC_OUT = "private.ms-lending-api.broker-request.local";

    public BrokerRequestEventPublisher(ObjectMapper mapper, KafkaManager kafkaManager) {
        this.mapper = mapper;
        this.kafkaManager = kafkaManager;
    }

    @Override
    public void publish(BrokerRequestEvent event) {
        log.debug("Request to publish event: {}", event);
        kafkaManager.publish(BROKER_REQUEST_TOPIC_OUT, event.key(), toPayload(event));
    }

    private String toPayload(BrokerRequestEvent event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Error on serializing event to json string. Event: {}", event);
            throw new RuntimeException(e);
        }
    }
}
