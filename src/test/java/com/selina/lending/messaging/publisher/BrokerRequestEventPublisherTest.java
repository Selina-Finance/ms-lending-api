///*
// * Copyright 2022 Selina Finance
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//
//package com.selina.lending.messaging.publisher;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.SettableListenableFuture;
//
//import static com.selina.lending.testHelper.BrokerRequestEventTestHelper.buildBrokerRequestFinishedEvent;
//import static com.selina.lending.testHelper.BrokerRequestEventTestHelper.buildBrokerRequestStartedEvent;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class BrokerRequestEventPublisherTest {
//
//    @Mock
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    @InjectMocks
//    private BrokerRequestEventPublisher publisher;
//
//    @Test
//    void shouldInvokeKafkaTemplateWithCorrectArgumentsWhenPublishStartedEvent() {
//        // Given
//        var event = buildBrokerRequestStartedEvent();
//
//        SettableListenableFuture<SendResult<String, Object>> future = new SettableListenableFuture<>();
//        when(kafkaTemplate.send(any(), any(), any())).thenReturn(future);
//
//        // When
//        publisher.publish(event);
//
//        // Then
//        verify(kafkaTemplate, times(1)).send("private.ms-lending-api.broker-request.local", event.requestId(), event);
//    }
//
//    @Test
//    void shouldInvokeKafkaTemplateWithCorrectArgumentsWhenPublishFinishedEvent() {
//        // Given
//        var event = buildBrokerRequestFinishedEvent();
//
//        SettableListenableFuture<SendResult<String, Object>> future = new SettableListenableFuture<>();
//        when(kafkaTemplate.send(any(), any(), any())).thenReturn(future);
//
//        // When
//        publisher.publish(event);
//
//        // Then
//        verify(kafkaTemplate, times(1)).send("private.ms-lending-api.broker-request.local", event.requestId(), event);
//    }
//
//}