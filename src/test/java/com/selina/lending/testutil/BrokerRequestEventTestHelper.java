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

package com.selina.lending.testutil;

import com.selina.lending.messaging.event.BrokerRequestEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class BrokerRequestEventTestHelper {
    public static final String SOURCE = "the broker";

    public static BrokerRequestEvent buildBrokerRequestKpiEvent() {
        return BrokerRequestEvent.builder()
                .source(SOURCE)
                .uriPath("/test")
                .httpMethod("GET")
                .ip("127.0.0.1")
                .started(Instant.now())
                .httpResponseCode(200)
                .finished(Instant.now().plus(1, ChronoUnit.MINUTES))
                .build();
    }

}
