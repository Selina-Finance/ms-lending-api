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

package com.selina.lending.api.interceptor;

import com.selina.lending.messaging.publisher.BrokerRequestEventPublisher;
import com.selina.lending.messaging.publisher.event.BrokerRequestCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(value = "kafka.enable", havingValue = "true", matchIfMissing = true)
public class BrokerRequestInterceptor implements HandlerInterceptor {

    private final BrokerRequestEventPublisher publisher;

    public BrokerRequestInterceptor(BrokerRequestEventPublisher publisher) {
        this.publisher = publisher;
    }

    // TODO: POC
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("========> BEFORE");

        var event = new BrokerRequestCreatedEvent(UUID.randomUUID(), "local-test", "BEFORE");
        publisher.publish(event);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("========> AFTER");

        var event = new BrokerRequestCreatedEvent(UUID.randomUUID(), "local-test", "AFTER");
        publisher.publish(event);

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
