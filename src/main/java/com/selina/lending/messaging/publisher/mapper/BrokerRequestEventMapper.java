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
import com.selina.lending.internal.dto.DIPApplicationResponse;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
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
            var requestBody = objectMapper.readValue(httpRequest.getContentAsByteArray(), DIPApplicationRequest.class);
            var responseBody = objectMapper.readValue(httpResponse.getContentAsByteArray(), DIPApplicationResponse.class);

            if (responseBody.getApplication() == null || responseBody.getApplication().getStatus() == null) {
                return Optional.empty();
            }

            String externalId = responseBody.getApplication().getExternalApplicationId();
            String decision = responseBody.getApplication().getStatus();

            return buildEvent(
                    httpRequest,
                    objectMapper.writeValueAsString(requestBody),
                    httpResponse,
                    objectMapper.writeValueAsString(responseBody),
                    started,
                    clientId,
                    externalId,
                    decision
            );
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
            var requestBody = objectMapper.readValue(httpRequest.getContentAsByteArray(), QuickQuoteApplicationRequest.class);
            var responseBody = objectMapper.readValue(httpResponse.getContentAsByteArray(), QuickQuoteResponse.class);
            String externalId = responseBody.getExternalApplicationId();
            String decision = responseBody.getStatus();

            if (externalId == null || decision == null) {
                return Optional.empty();
            }

            return buildEvent(
                    httpRequest,
                    objectMapper.writeValueAsString(requestBody),
                    httpResponse,
                    objectMapper.writeValueAsString(responseBody),
                    started,
                    clientId,
                    externalId,
                    decision
            );
        } catch (IOException e) {
            log.error("Can't map event. Reason: {}", e.getMessage());
            return Optional.empty();
        }

    }

    @NotNull
    private Optional<BrokerRequestKpiEvent> buildEvent(ContentCachingRequestWrapper httpRequest,
                                                       String requestBody,
                                                       ContentCachingResponseWrapper httpResponse,
                                                       String responseBody,
                                                       Instant started,
                                                       String clientId,
                                                       String externalId,
                                                       String decision
    ) {
        var requestId = Optional.ofNullable(httpRequest.getHeader(REQUEST_ID_HEADER_NAME)).orElse(UUID.randomUUID().toString());

        return Optional.of(BrokerRequestKpiEvent.builder()
                .source(clientId)
                .uriPath(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .httpRequestBody(requestBody)
                .httpResponseBody(responseBody)
                .ip(getRemoteAddr(httpRequest))
                .started(started)
                .finished(Instant.now())
                .httpResponseCode(httpResponse.getStatus())
                .build());
    }

    public Optional<BrokerRequestKpiEvent> toEvent(
            ContentCachingRequestWrapper request,
            ContentCachingResponseWrapper response,
            Instant started,
            String source) {
        try {


            return Optional.of(BrokerRequestKpiEvent.builder()
                    .ip(getRemoteAddr(request))
                    .source(source)
                    .uriPath(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .httpRequestBody(requestBody)
                    .httpResponseBody(responseBody)
                    .httpResponseCode(response.getStatus())
                    .started(started)
                    .finished(Instant.now())
                    .build());


        } catch (Exception e) {
            log.error("Can't map event. Reason: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
