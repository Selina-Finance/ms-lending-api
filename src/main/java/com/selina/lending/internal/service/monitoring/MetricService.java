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

package com.selina.lending.internal.service.monitoring;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetricService {

    private final MeterRegistry meterRegistry;
    private final Counter applicationDeleteFailedCounter;

    public MetricService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        applicationDeleteFailedCounter = this.meterRegistry.counter("application_delete_failed_counter");
    }

    public void incrementApplicationDeleteFailed() {
        log.debug("Increment applicationDeleteFailedCounter");
        applicationDeleteFailedCounter.increment();
    }
}
