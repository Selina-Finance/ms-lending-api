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
import com.selina.lending.messaging.publisher.mapper.BrokerRequestEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class BrokerRequestKpiResolver {

    private final BrokerRequestEventPublisher publisher;
    private final BrokerRequestEventMapper mapper;

    public BrokerRequestKpiResolver(BrokerRequestEventPublisher publisher, BrokerRequestEventMapper mapper) {
        this.publisher = publisher;
        this.mapper = mapper;
    }

    public void onRequestStarted(String broker, String requestId, HttpServletRequest httpRequest) {
        log.debug("Handling started broker request. Broker: {}, RequestId: {}", broker, requestId);

        var event = mapper.toStartedEvent(broker, requestId, httpRequest);
        publisher.publish(event);
    }

    public void onRequestFinished(String requestId, Integer httpResponseCode) {
        log.debug("Handling finished broker request. RequestId: {}, HttpStatus: {}", requestId, httpResponseCode);

        var event = mapper.toFinishedEvent(requestId, httpResponseCode);
        publisher.publish(event);
    }

}
