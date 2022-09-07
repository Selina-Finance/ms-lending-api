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

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(value = "kafka.enable", havingValue = "true", matchIfMissing = true)
public class BrokerRequestInterceptor implements HandlerInterceptor {

    private final BrokerRequestKpiResolver kpiResolver;
    private static final String CLIENT_ID_JWT_CLAIM_NAME = "clientId";
    private static final String REQUEST_ID_HEADER_NAME = "x-selina-request-id";
    private static final String REQUEST_ID_ATTR_NAME = "broker-request-id";

    public BrokerRequestInterceptor(BrokerRequestKpiResolver kpiResolver) {
        this.kpiResolver = kpiResolver;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = Optional
                .ofNullable(request.getHeader(REQUEST_ID_HEADER_NAME))
                .orElse(UUID.randomUUID().toString());
        request.setAttribute(REQUEST_ID_ATTR_NAME, requestId);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var jwt = (Jwt) authentication.getPrincipal();
        String brokerClientId = (String) jwt.getClaims().get(CLIENT_ID_JWT_CLAIM_NAME);

        kpiResolver.onRequestStarted(brokerClientId, requestId, request);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // do nothing. All post-request work will be done inside afterCompletion httpMethod
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String brokerRequestId = (String) request.getAttribute(REQUEST_ID_ATTR_NAME);
        int httpResponseCode = response.getStatus();

        if (brokerRequestId != null) {
            kpiResolver.onRequestFinished(brokerRequestId, httpResponseCode);
        }

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
