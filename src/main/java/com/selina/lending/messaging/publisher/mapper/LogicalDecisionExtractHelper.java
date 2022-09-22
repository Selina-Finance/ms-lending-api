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
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class LogicalDecisionExtractHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<String> extractLogicalDecision(HttpServletResponse response) {
        try {
//            ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);
//            resp.copyBodyToResponse();
//            var body = objectMapper.readValue(resp.getContentAsByteArray(), ApplicationResponse.class);
//            return Optional.ofNullable(body.getApplication().getStatus());
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
