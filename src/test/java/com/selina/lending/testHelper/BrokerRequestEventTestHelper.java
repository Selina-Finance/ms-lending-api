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

package com.selina.lending.testHelper;

import com.selina.lending.messaging.event.BrokerRequestFinishedEvent;
import com.selina.lending.messaging.event.BrokerRequestStartedEvent;

import java.time.Instant;
import java.util.UUID;

public class BrokerRequestEventTestHelper {
    public static final String SOURCE = "the broker";

    public static BrokerRequestStartedEvent buildBrokerRequestStartedEvent() {
        return BrokerRequestStartedEvent.builder()
                .requestId(UUID.randomUUID().toString())
                .externalApplicationId(UUID.randomUUID().toString())
                .created(Instant.now())
                .source(SOURCE)
                .uriPath("/test")
                .httpMethod("GET")
                .ip("127.0.0.1")
                .build();
    }

    public static BrokerRequestFinishedEvent buildBrokerRequestFinishedEvent() {
        return BrokerRequestFinishedEvent.builder()
                .requestId(UUID.randomUUID().toString())
                .decision(null)
                .httpResponseCode(200)
                .created(Instant.now())
                .build();
    }
}
