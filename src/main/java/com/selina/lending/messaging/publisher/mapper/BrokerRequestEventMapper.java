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
import com.selina.lending.messaging.event.BrokerRequestKpiEvent;
import lombok.extern.slf4j.Slf4j;
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


    public Optional<BrokerRequestKpiEvent> toBrokerRequestKpiEvent(
            ContentCachingRequestWrapper request,
            ContentCachingResponseWrapper response,
            Instant started,
            String clientId
    ) {
        String requestId = Optional.ofNullable(request.getHeader(REQUEST_ID_HEADER_NAME)).orElse(UUID.randomUUID().toString());

//        String requestBody = getStringValue(request.getContentAsByteArray(), request.getCharacterEncoding());
//        String responseBody = getStringValue(response.getContentAsByteArray(), response.getCharacterEncoding());

        try {
            ApplicationResponse resp = objectMapper.readValue(response.getContentAsByteArray(), ApplicationResponse.class);

            return Optional.of(BrokerRequestKpiEvent.builder()
                    .requestId(requestId)
                    .externalApplicationId(resp.getApplication().getExternalApplicationId())
                    .source(clientId)
                    .uriPath(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .ip(getRemoteAddr(request))
                    .started(started)
                    .finished(Instant.now())
                    .decision(resp.getApplication().getStatus())
                    .httpResponseCode(response.getStatus())
                    .build());
        } catch (IOException e) {
            log.error("Can't map event. Reason: {}", e.getMessage());
            return Optional.empty();
        }
    }


}
