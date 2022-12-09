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

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.api.MiddlewareApplicationServiceApi;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;
import com.selina.lending.internal.service.monitoring.MetricService;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MiddlewareApplicationServiceRepositoryImpl implements MiddlewareApplicationServiceRepository {

    public static final String DELETE_FAILED_ERROR =
            "**ALERT** Unable to delete application [externalApplicationId=%s] [sourceAccount=%s]";
    private final MiddlewareApplicationServiceApi middlewareApplicationServiceApi;

    private final MetricService metricService;

    public MiddlewareApplicationServiceRepositoryImpl(MiddlewareApplicationServiceApi middlewareApplicationServiceApi,
                                                      MetricService metricService) {
        this.middlewareApplicationServiceApi = middlewareApplicationServiceApi;
        this.metricService = metricService;
    }

    @CircuitBreaker(name = "middleware-application-service-cb", fallbackMethod = "middlewareGetByExternalIdApiFallback")
    @Override
    public ApplicationIdentifier getAppIdByExternalId(String externalAppId) {
        log.info("Request to get application Id by [externalApplicationId={}]", externalAppId);
        return middlewareApplicationServiceApi.getApplicationIdByExternalApplicationId(externalAppId);
    }

    @Retry(name = "middleware-application-service-retry", fallbackMethod = "deleteApiFallback")
    @Override
    @Async("taskExecutor")
    public void deleteAppByExternalApplicationId(String sourceAccount, String externalAppId) {
        log.info("Request to delete application by [externalApplicationId={}] [sourceAccount={}]", externalAppId, sourceAccount);
        middlewareApplicationServiceApi.deleteApplicationByExternalApplicationId(sourceAccount, externalAppId);
        log.info("Application deleted [externalApplicationId={}] [sourceAccount={}]", externalAppId, sourceAccount);
    }

    private void deleteApiFallback(String sourceAccount, String externalApplicationId, FeignException.FeignServerException e) { //NOSONAR
        log.error(String.format(DELETE_FAILED_ERROR, externalApplicationId, sourceAccount), e);
        metricService.incrementApplicationDeleteFailed();
    }

    private void deleteApiFallback(String sourceAccount, String externalApplicationId, feign.RetryableException e) { //NOSONAR
        log.error(String.format(DELETE_FAILED_ERROR, externalApplicationId, sourceAccount), e);
        metricService.incrementApplicationDeleteFailed();
    }

    private ApplicationIdentifier middlewareGetByExternalIdApiFallback(CallNotPermittedException e) { //NOSONAR
       throw remoteResourceProblemException(e);
    }

    private RemoteResourceProblemException remoteResourceProblemException(Exception e) {
        log.error("Middleware application service is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
