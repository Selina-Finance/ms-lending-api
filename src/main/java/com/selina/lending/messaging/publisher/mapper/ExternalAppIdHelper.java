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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExternalAppIdHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<String> getExternalAppId(HttpServletRequest request) {

        if (request.getRequestURI().contains("/application")) {
            if ("GET".equals(request.getMethod())) {
                String[] numbers = request.getRequestURI().split("/");
                return Optional.ofNullable(numbers[numbers.length - 1]);
            }
            if ("PUT".equals(request.getMethod())) {
                String[] numbers = request.getRequestURI().split("/");
                return Optional.ofNullable(numbers[numbers.length - 2]);
            }
            if ("POST".equals(request.getMethod())) {
                try {
                    String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                    Map map = objectMapper.readValue(body, Map.class);
                    return Optional.ofNullable((String) map.get("externalApplicationId"));
                } catch (Exception e) {
                    return Optional.empty();
                }
            }
        }

        return Optional.empty();
    }

}
