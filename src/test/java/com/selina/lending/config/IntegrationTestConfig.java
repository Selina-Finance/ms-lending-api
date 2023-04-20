package com.selina.lending.config;

import com.selina.lending.messaging.publisher.MiddlewareCreateApplicationEventPublisher;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class IntegrationTestConfig {

    @Bean
    public MiddlewareCreateApplicationEventPublisher middlewareCreateApplicationEventPublisher() {
        return mock(MiddlewareCreateApplicationEventPublisher.class);
    }
}
