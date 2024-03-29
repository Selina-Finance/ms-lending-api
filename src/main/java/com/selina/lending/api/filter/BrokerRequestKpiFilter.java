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

package com.selina.lending.api.filter;

import com.selina.lending.service.BrokerRequestKpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

import static com.selina.lending.config.security.SecurityConfig.ACTUATOR_URL;
import static com.selina.lending.config.security.SecurityConfig.API_DOCS_URL;
import static com.selina.lending.config.security.SecurityConfig.LOGIN_URL;
import static com.selina.lending.config.security.SecurityConfig.SWAGGER_URL;

@Slf4j
@Component
@Order(2)
@ConditionalOnProperty(value = "kafka.enable", havingValue = "true", matchIfMissing = true)
public class BrokerRequestKpiFilter extends OncePerRequestFilter {

    private final BrokerRequestKpiService kpiResolver;

    public BrokerRequestKpiFilter(BrokerRequestKpiService kpiResolver) {
        this.kpiResolver = kpiResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isTracked(request)) {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            var started = Instant.now();

            filterChain.doFilter(requestWrapper, responseWrapper); // the request will process here. it's time-consuming

            kpiResolver.handle(requestWrapper, responseWrapper, started);
            responseWrapper.copyBodyToResponse();
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private static boolean isTracked(HttpServletRequest request) {
        return isNotUnderUrl(request.getRequestURI(), LOGIN_URL)
                && isNotUnderUrl(request.getRequestURI(), ACTUATOR_URL)
                && isNotUnderUrl(request.getRequestURI(), SWAGGER_URL)
                && isNotUnderUrl(request.getRequestURI(), API_DOCS_URL);
    }

    private static boolean isNotUnderUrl(String requestUri, String url) {
        return !requestUri.startsWith(url);
    }
}
