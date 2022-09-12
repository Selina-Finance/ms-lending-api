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

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.api.MiddlewareGetApi;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MiddlewareRepositoryImpl implements MiddlewareRepository {
    private final MiddlewareApi middlewareApi;
    private final MiddlewareGetApi middlewareGetApi;

    public MiddlewareRepositoryImpl(MiddlewareApi middlewareApi, MiddlewareGetApi middlewareGetApi) {
        this.middlewareApi = middlewareApi;
        this.middlewareGetApi = middlewareGetApi;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareGetApiFallback")
    @Override
    public Optional<ApplicationDecisionResponse> getApplicationById(String id) {
        log.debug("Request to get application by id: {}", id);
        return Optional.of(middlewareApi.getApplicationById(id));
    }

    @CircuitBreaker(name = "middleware-get-api-cb", fallbackMethod = "middlewareGetApiFallback")
    @Override
    public Optional<ApplicationIdentifier> getApplicationIdByExternalApplicationId(String externalApplicationId) {
        return Optional.of(middlewareGetApi.getApplicationIdByExternalApplicationId(externalApplicationId));
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallbackDefault")
    @Override
    public void updateDipApplication(String id, ApplicationRequest applicationRequest) {
        log.debug("Update dip application for id: {}, applicationRequest {} ", id, applicationRequest);
        middlewareApi.updateDipApplication(id, applicationRequest);
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationResponse createDipApplication(ApplicationRequest applicationRequest) {
        log.debug("Create dip application applicationRequest {}", applicationRequest);

        var appResponse =  middlewareApi.createDipApplication(applicationRequest);

        log.info("Finished calling mw to create dip application id {}", appResponse.getApplicationId());
        return appResponse;
    }

    private Optional<ApplicationDecisionResponse> middlewareGetApiFallback(FeignException.FeignServerException e) {
        defaultMiddlewareFallback(e);
        return Optional.empty();
    }

    private Optional<ApplicationDecisionResponse> middlewareGetApiFallback(feign.RetryableException e) {
        defaultMiddlewareFallback(e);
        return Optional.empty();
    }

    private void middlewareApiFallbackDefault(FeignException.FeignServerException e) {
        defaultMiddlewareFallback(e);
    }

    private void middlewareApiFallbackDefault(feign.RetryableException e) {
        defaultMiddlewareFallback(e);
    }

    private ApplicationResponse middlewareApiFallback(FeignException.FeignServerException e) {
        defaultMiddlewareFallback(e);
        return ApplicationResponse.builder().build();
    }

    private ApplicationResponse middlewareApiFallback(feign.RetryableException e) {
        defaultMiddlewareFallback(e);
        return ApplicationResponse.builder().build();
    }

    private void defaultMiddlewareFallback(Exception e) {
        log.error("Middleware is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
