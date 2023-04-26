package com.selina.lending.messaging.publisher;

import com.selina.lending.internal.exception.KafkaSendEventException;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.quote.Product;
import com.selina.lending.messaging.event.middleware.MiddlewareCreateApplicationEvent;
import com.selina.lending.messaging.mapper.middleware.MiddlewareCreateApplicationEventMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"kafka.enable=true"})
@EmbeddedKafka(partitions = 1, topics = "${kafka.topics.middlewareCreateApplication.name}", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
public class MiddlewareCreateApplicationEventPublisherTest extends MapperBase {

    @Value("${kafka.topics.middlewareCreateApplication.name}")
    private String topic;

    @MockBean
    private TokenService tokenService;

    @SpyBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MiddlewareCreateApplicationEventMapper mapper;

    @Autowired
    private MiddlewareCreateApplicationEventPublisher publisher;

    @Autowired
    private EmbeddedKafkaBroker kafkaBroker;

    @Test
    void shouldSendMiddlewareCreateApplicationEvent() {
        // Given
        var applicationRequest = getQuickQuoteApplicationRequestDto();
        var products = List.of(getProduct());
        var middlewareCreateApplicationEvent = mapper.mapToMiddlewareCreateApplicationEvent(applicationRequest, products);

        var consumer = getKafkaConsumer();
        consumer.subscribe(Collections.singleton(topic));

        // When
        publisher.publish(middlewareCreateApplicationEvent);
        var consumerRecord = KafkaTestUtils.getSingleRecord(consumer, topic, 5000);
        var event = consumerRecord.value();

        // Then
        assertThat(event, equalTo(middlewareCreateApplicationEvent));
    }

    @Test
    void whenGetErrorSendingKafkaEventThenThrowKafkaSendEventException() {
        // Given
        var applicationRequest = getQuickQuoteApplicationRequestDto();
        var products = List.of(getProduct());
        var middlewareCreateApplicationEvent = mapper.mapToMiddlewareCreateApplicationEvent(applicationRequest, products);
        when(kafkaTemplate.send(topic, middlewareCreateApplicationEvent)).thenThrow(new RuntimeException("Unexpected exception"));

        // When
        // Then
        var exception = assertThrows(KafkaSendEventException.class, () -> publisher.publish(middlewareCreateApplicationEvent));

        assertThat(exception.getMessage(), equalTo("Error sending MiddlewareCreateApplicationEvent"));
        assertThat(exception.getCause().getMessage(), equalTo("Unexpected exception"));
    }

    private Consumer<String, MiddlewareCreateApplicationEvent> getKafkaConsumer() {
        Map<String, Object> consumerConfig = KafkaTestUtils.consumerProps("consumerGroup", "false", kafkaBroker);
        JsonDeserializer<MiddlewareCreateApplicationEvent> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("com.selina.lending.messaging.event.middleware");
        return new DefaultKafkaConsumerFactory<>(consumerConfig, new StringDeserializer(), jsonDeserializer).createConsumer();
    }
}
