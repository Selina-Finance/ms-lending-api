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

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MiddlewareRepositoryImpl implements MiddlewareRepository {
    private final MiddlewareApi middlewareApi;

    public MiddlewareRepositoryImpl(MiddlewareApi middlewareApi) {
        this.middlewareApi = middlewareApi;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareGetApiFallback")
    @Override
    public Optional<ApplicationDecisionResponse> getApplicationById(String id) {
        log.debug("Request to get application by id: {}", id);
        return Optional.of(middlewareApi.getApplicationById(id));
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallbackDefault")
    @Override
    public void updateDipApplication(String id, ApplicationRequest applicationRequest) {
        log.debug("Update dip application for id: {}, applicationRequest {} ", id, applicationRequest);
        middlewareApi.updateDipApplication(id, applicationRequest);
        log.info("Finished calling mw to update dip application");
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationResponse createDipApplication(ApplicationRequest applicationRequest) {
        log.debug("Create dip application applicationRequest {}", applicationRequest);

        var appResponse =  middlewareApi.createDipApplication(applicationRequest);

        log.info("Finished calling mw to create dip application");
        return appResponse;
    }

    public ApplicationResponse middlewareApiFallback(Exception e) {
        defaultMiddlewareFallback(e);
        return ApplicationResponse.builder().build();
    }

    public Optional<ApplicationDecisionResponse> middlewareGetApiFallback(Exception e) {
        defaultMiddlewareFallback(e);
        return Optional.empty();
    }

    public void middlewareApiFallbackDefault(Exception e) {
        defaultMiddlewareFallback(e);
    }

    private static void defaultMiddlewareFallback(Exception e) {
        log.error("Middleware is unavailable. {}", e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
