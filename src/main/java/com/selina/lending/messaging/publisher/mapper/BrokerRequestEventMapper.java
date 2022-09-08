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

import com.selina.lending.messaging.event.BrokerRequestFinishedEvent;
import com.selina.lending.messaging.event.BrokerRequestStartedEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Optional;

import static com.selina.lending.messaging.publisher.mapper.ExternalAppIdHelper.getExternalAppId;

@Slf4j
@Component
public class BrokerRequestEventMapper {

    public BrokerRequestStartedEvent toStartedEvent(String broker, String requestId, HttpServletRequest httpRequest) {
        return BrokerRequestStartedEvent.builder()
                .requestId(requestId)
                .externalApplicationId(fetchExternalApplicationId(httpRequest))
                .created(Instant.now())
                .source(broker)
                .uriPath(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .ip(getRemoteAddr(httpRequest))
                .build();
    }

    private String fetchExternalApplicationId(HttpServletRequest httpRequest) {
        Optional<String> optExternalAppId = getExternalAppId(httpRequest);
        if (optExternalAppId.isEmpty()) {
            log.warn("Can't fetch externalAppId from: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
        }

        return optExternalAppId.orElse(null);
    }

    private String getRemoteAddr(@NotNull HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }

    public BrokerRequestFinishedEvent toFinishedEvent(String requestId, int httpResponseCode) {
        return new BrokerRequestFinishedEvent(requestId, httpResponseCode, Instant.now());
    }
}
