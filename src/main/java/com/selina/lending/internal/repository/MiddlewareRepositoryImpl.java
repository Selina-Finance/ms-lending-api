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

package com.selina.lending.internal.repository;

import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MiddlewareRepositoryImpl implements MiddlewareRepository {
    private final MiddlewareApi middlewareApi;

    public MiddlewareRepositoryImpl(MiddlewareApi middlewareApi) {
        this.middlewareApi = middlewareApi;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationDecisionResponse getApplicationById(String id) {
        log.debug("Request to get application by id: {}", id);
        return middlewareApi.getApplicationById(id);
    }

    @Override
    public ApplicationResponse updateDipApplication(String id, ApplicationRequest applicationRequest) {
        return null;
    }

    @Override
    public ApplicationResponse createDipApplication(ApplicationRequest applicationRequest) {
        return null;
    }

    public ApplicationResponse middlewareApiFallback(Exception e) {
        log.debug("Remote service is unavailable. Returning fallback.");
        return new ApplicationResponse();
    }
}
