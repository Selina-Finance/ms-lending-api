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

package com.selina.lending.service;

import com.selina.lending.messaging.mapper.BrokerRequestKpiEventMapper;
import com.selina.lending.messaging.publisher.BrokerRequestKpiEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.time.Instant;

@Slf4j
@Component
@ConditionalOnProperty(value = "kafka.enable", havingValue = "true", matchIfMissing = true)
public class BrokerRequestKpiService {
    
    private final BrokerRequestKpiEventPublisher publisher;
    private final BrokerRequestKpiEventMapper mapper;
    private final TokenService tokenService;

    public BrokerRequestKpiService(BrokerRequestKpiEventPublisher publisher, BrokerRequestKpiEventMapper mapper, TokenService tokenService) {
        this.publisher = publisher;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    public void handle(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, Instant started) {
        var optEvent = mapper.toEvent(request, response, started, tokenService.retrieveSourceAccount());

        optEvent.ifPresentOrElse(
                publisher::publish,
                () -> log.warn("BrokerRequestKpiEvent won't be published due to mapping problem")
        );
    }

}
