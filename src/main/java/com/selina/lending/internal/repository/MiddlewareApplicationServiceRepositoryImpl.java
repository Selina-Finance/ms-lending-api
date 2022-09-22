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

import org.springframework.stereotype.Service;

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.api.MiddlewareApplicationServiceApi;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MiddlewareApplicationServiceRepositoryImpl implements MiddlewareApplicationServiceRepository {

    public static final String DELETE_FAILED_ERROR =
            "**ALERT** Unable to delete application externalApplicationId %s, sourceAccount %s";
    private final MiddlewareApplicationServiceApi middlewareApplicationServiceApi;

    public MiddlewareApplicationServiceRepositoryImpl(MiddlewareApplicationServiceApi middlewareApplicationServiceApi) {
        this.middlewareApplicationServiceApi = middlewareApplicationServiceApi;
    }

    @CircuitBreaker(name = "middleware-application-service-cb", fallbackMethod = "middlewareGetByExternalIdApiFallback")
    @Override
    public ApplicationIdentifier getApplicationSourceAccountByExternalApplicationId(String externalApplicationId) {
        log.info("Request to get application source by externalApplicationId {}", externalApplicationId);
        return middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(externalApplicationId);
    }

    @CircuitBreaker(name = "middleware-application-service-cb", fallbackMethod = "middlewareGetByExternalIdApiFallback")
    @Override
    public ApplicationIdentifier getApplicationIdByExternalApplicationId(String externalApplicationId) {
        log.info("Request to get application Id by externalApplicationId {}", externalApplicationId);
        return middlewareApplicationServiceApi.getApplicationIdByExternalApplicationId(externalApplicationId);
    }

    @Retry(name = "middleware-application-service-retry", fallbackMethod = "deleteApiFallback")
    @Override
    public void deleteApplicationByExternalApplicationId(String sourceAccount, String externalApplicationId) {
        log.info("Request to delete application by externalApplicationId {}, sourceAccount {}", externalApplicationId, sourceAccount);
        middlewareApplicationServiceApi.deleteApplicationByExternalApplicationId(sourceAccount, externalApplicationId);
    }

    private void deleteApiFallback(String sourceAccount, String externalApplicationId, FeignException.FeignServerException e) { //NOSONAR
        log.error(String.format(DELETE_FAILED_ERROR, externalApplicationId, sourceAccount), e);
    }

    private void deleteApiFallback(String sourceAccount, String externalApplicationId, feign.RetryableException e) { //NOSONAR
        log.error(String.format(DELETE_FAILED_ERROR, externalApplicationId, sourceAccount), e);
    }

    private ApplicationIdentifier middlewareGetByExternalIdApiFallback(FeignException.FeignServerException e) { //NOSONAR
        defaultFallback(e);
        return ApplicationIdentifier.builder().build();
    }

    private ApplicationIdentifier middlewareGetByExternalIdApiFallback(feign.RetryableException e) { //NOSONAR
        defaultFallback(e);
        return ApplicationIdentifier.builder().build();
    }

    private void defaultFallback(Exception e) {
        log.error("Middleware application service is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
