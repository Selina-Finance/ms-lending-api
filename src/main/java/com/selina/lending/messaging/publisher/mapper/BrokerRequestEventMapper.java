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

package com.selina.lending.messaging.publisher.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import com.selina.lending.messaging.event.BrokerRequestKpiEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.selina.lending.messaging.publisher.mapper.IPHelper.getRemoteAddr;

@Slf4j
@Component
public class BrokerRequestEventMapper {

    private static final String REQUEST_ID_HEADER_NAME = "x-selina-request-id";
    private final ObjectMapper objectMapper;

    public BrokerRequestEventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<BrokerRequestKpiEvent> dipToKpiEvent(ContentCachingRequestWrapper httpRequest,
                                                         ContentCachingResponseWrapper httpResponse,
                                                         Instant started,
                                                         String clientId) {
        try {
            var resp = objectMapper.readValue(httpResponse.getContentAsByteArray(), ApplicationResponse.class);

            if (resp.getApplication() == null || resp.getApplication().getStatus() == null) {
                return Optional.empty();
            }

            String externalId = resp.getApplication().getExternalApplicationId();
            String decision = resp.getApplication().getStatus();

            return doMapping(httpRequest, httpResponse, started, clientId, externalId, decision);
        } catch (IOException e) {
            log.error("Can't map event. Reason: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<BrokerRequestKpiEvent> quickQuoteToKpiEvent(ContentCachingRequestWrapper httpRequest,
                                                                ContentCachingResponseWrapper httpResponse,
                                                                Instant started,
                                                                String clientId) {
        try {
            var resp = objectMapper.readValue(httpResponse.getContentAsByteArray(), QuickQuoteResponse.class);
            String externalId = resp.getExternalApplicationId();
            String decision = resp.getStatus();

            return doMapping(httpRequest, httpResponse, started, clientId, externalId, decision);
        } catch (IOException e) {
            log.error("Can't map event. Reason: {}", e.getMessage());
            return Optional.empty();
        }

    }

    @NotNull
    private static Optional<BrokerRequestKpiEvent> doMapping(ContentCachingRequestWrapper httpRequest,
                                                             ContentCachingResponseWrapper httpResponse,
                                                             Instant started, String clientId,
                                                             String externalId,
                                                             String decision
    ) {
        var requestId = Optional.ofNullable(httpRequest.getHeader(REQUEST_ID_HEADER_NAME)).orElse(UUID.randomUUID().toString());

        return Optional.of(BrokerRequestKpiEvent.builder()
                .requestId(requestId)
                .externalApplicationId(externalId)
                .source(clientId)
                .uriPath(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .ip(getRemoteAddr(httpRequest))
                .started(started)
                .finished(Instant.now())
                .decision(decision)
                .httpResponseCode(httpResponse.getStatus())
                .build());
    }

}
