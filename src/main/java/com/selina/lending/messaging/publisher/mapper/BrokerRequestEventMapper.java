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

import com.selina.lending.messaging.event.BrokerRequestKpiEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

import static com.selina.lending.messaging.publisher.mapper.IPHelper.getRemoteAddr;

@Slf4j
@Component
public class BrokerRequestEventMapper {

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
                    .httpRequestBody(byteToString(request.getContentAsByteArray(), request.getCharacterEncoding()))
                    .httpResponseBody(new String(response.getContentAsByteArray(), response.getCharacterEncoding()))
                    .httpResponseCode(response.getStatus())
                    .started(started)
                    .finished(Instant.now())
                    .build());

        } catch (Exception e) {
            log.error("Can't map kpi event. Reason: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private String byteToString(byte[] bytes, String encoding) throws UnsupportedEncodingException {
        return new String(
                (bytes != null) ? bytes : new byte[]{},
                (encoding != null) ? encoding : StandardCharsets.UTF_8.name()
        );
    }
}
