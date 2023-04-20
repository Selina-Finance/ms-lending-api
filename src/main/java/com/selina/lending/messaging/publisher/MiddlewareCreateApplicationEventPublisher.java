package com.selina.lending.messaging.publisher;

import com.selina.lending.internal.exception.KafkaSendEventException;
import com.selina.lending.messaging.event.middleware.MiddlewareCreateApplicationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "kafka.enable", havingValue = "true", matchIfMissing = true)
public class MiddlewareCreateApplicationEventPublisher {

    private static final String ERROR_SENDING_MIDDLEWARE_CREATE_APPLICATION_EVENT = "Error sending MiddlewareCreateApplicationEvent";

    private final String topic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MiddlewareCreateApplicationEventPublisher(@Value(value = "${kafka.topics.middlewareCreateApplication.name}") String topic,
                                                     KafkaTemplate<String, Object> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(MiddlewareCreateApplicationEvent event) {
        try {
            log.debug("Send MiddlewareCreateApplicationEvent [event={}]", event);
            kafkaTemplate.send(topic, event).get().getRecordMetadata();
        } catch (Exception ex) {
            log.error(ERROR_SENDING_MIDDLEWARE_CREATE_APPLICATION_EVENT + " [event={}]", event, ex);
            throw new KafkaSendEventException(ERROR_SENDING_MIDDLEWARE_CREATE_APPLICATION_EVENT, ex);
        }
    }
}
