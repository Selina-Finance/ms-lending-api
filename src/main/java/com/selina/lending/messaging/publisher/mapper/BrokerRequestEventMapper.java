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

import static com.selina.lending.messaging.publisher.mapper.IPHelper.getRemoteAddr;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import com.selina.lending.messaging.event.BrokerRequestKpiEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BrokerRequestEventMapper {

    private static final String REQUEST_ID_HEADER_NAME = "x-selina-request-id";
    private static final String QUICK_QUOTE_PATH = "quickquote";
    private final ObjectMapper objectMapper;

    public BrokerRequestEventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<BrokerRequestKpiEvent> toBrokerRequestKpiEvent(
            ContentCachingRequestWrapper httpRequest,
            ContentCachingResponseWrapper httpResponse,
            Instant started,
            String clientId) {
        String requestId = Optional.ofNullable(httpRequest.getHeader(REQUEST_ID_HEADER_NAME)).orElse(UUID.randomUUID().toString());

        try {
            Pair<String, String> externalAppIdDecision = getExternalApplicationIdAndDecisionPair(httpRequest, httpResponse);

            return (externalAppIdDecision.getLeft() == null || externalAppIdDecision.getRight() == null) ?
                    Optional.empty() :
                    Optional.of(BrokerRequestKpiEvent.builder()
                            .requestId(requestId)
                            .externalApplicationId(externalAppIdDecision.getLeft())
                            .source(clientId)
                            .uriPath(httpRequest.getRequestURI())
                            .httpMethod(httpRequest.getMethod())
                            .ip(getRemoteAddr(httpRequest))
                            .started(started)
                            .finished(Instant.now())
                            .decision(externalAppIdDecision.getRight())
                            .httpResponseCode(httpResponse.getStatus())
                            .build());
        } catch (IOException e) {
            log.error("Can't map event. Reason: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private Pair<String, String> getExternalApplicationIdAndDecisionPair(ContentCachingRequestWrapper httpRequest, ContentCachingResponseWrapper httpResponse)
            throws IOException {
        String externalApplicationId = null;
        String decision = null;

        if (isQuickQuoteRequest(httpRequest)) {
            var resp = objectMapper.readValue(httpResponse.getContentAsByteArray(), QuickQuoteResponse.class);
            externalApplicationId = resp.getExternalApplicationId();
            decision = resp.getStatus();
        } else {
            var resp = objectMapper.readValue(httpResponse.getContentAsByteArray(), ApplicationResponse.class);
            if (resp.getApplication() != null) {
                externalApplicationId = resp.getApplication().getExternalApplicationId();
                decision = resp.getApplication().getStatus();
            }
        }
        return Pair.of(externalApplicationId, decision);
    }

    private boolean isQuickQuoteRequest(ContentCachingRequestWrapper httpRequest) {
        return httpRequest.getRequestURI().contains(QUICK_QUOTE_PATH);
    }
}
