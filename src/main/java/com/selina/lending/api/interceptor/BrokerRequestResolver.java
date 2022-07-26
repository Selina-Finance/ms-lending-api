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

import com.selina.lending.internal.service.TokenService;
import com.selina.lending.messaging.event.BrokerRequestKpiEvent;
import com.selina.lending.messaging.publisher.BrokerRequestEventPublisher;
import com.selina.lending.messaging.publisher.mapper.BrokerRequestEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(value = "kafka.enable", havingValue = "true", matchIfMissing = true)
public class BrokerRequestResolver {
    private static final String QUICK_QUOTE_PATH = "quickquote";

    private final BrokerRequestEventPublisher publisher;
    private final BrokerRequestEventMapper mapper;
    private final TokenService tokenService;

    public BrokerRequestResolver(BrokerRequestEventPublisher publisher, BrokerRequestEventMapper mapper, TokenService tokenService) {
        this.publisher = publisher;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    public void handle(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, Instant started) {
        var optEvent = isQuickQuoteRequest(request)
                ? mapper.quickQuoteToKpiEvent(request, response, started, tokenService.retrieveSourceAccount())
                : mapper.dipToKpiEvent(request, response, started, tokenService.retrieveSourceAccount());

        optEvent.ifPresentOrElse(
                publisher::publish,
                () -> log.warn("BrokerRequestKpiEvent won't be published due to mapping problem")
        );
    }


    private boolean isQuickQuoteRequest(ContentCachingRequestWrapper httpRequest) {
        return httpRequest.getRequestURI().contains(QUICK_QUOTE_PATH);
    }

}
