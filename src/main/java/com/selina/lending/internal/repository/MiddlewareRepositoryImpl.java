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

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.api.MiddlewareApplicationServiceApi;
import com.selina.lending.internal.service.TokenService;
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
    private final MiddlewareApplicationServiceApi middlewareApplicationServiceApi;

    private final TokenService tokenService;

    public MiddlewareRepositoryImpl(MiddlewareApi middlewareApi, MiddlewareApplicationServiceApi middlewareApplicationServiceApi, TokenService tokenService) {
        this.middlewareApi = middlewareApi;
        this.middlewareApplicationServiceApi = middlewareApplicationServiceApi;
        this.tokenService = tokenService;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareGetApiFallback")
    private Optional<ApplicationDecisionResponse> getApplicationById(String id) {
        log.debug("Request to get application by id: {}", id);
        return Optional.of(middlewareApi.getApplicationById(id));
    }

    @CircuitBreaker(name = "middleware-application-service-cb", fallbackMethod = "middlewareGetByExternalIdApiFallback")
    private ApplicationIdentifier getApplicationIdByExternalApplicationId(String externalApplicationId) {
        log.debug("Request to get application id by external application id {}", externalApplicationId);
        return middlewareApplicationServiceApi.getApplicationIdByExternalApplicationId(externalApplicationId);
    }

    @CircuitBreaker(name = "middleware-application-service-cb", fallbackMethod = "middlewareGetByExternalIdApiFallback")
    private ApplicationIdentifier getApplicationSourceAccountByExternalApplicationId(String externalApplicationId) {
        log.debug("Request to get application source by external application id {}", externalApplicationId);
        return middlewareApplicationServiceApi.getApplicationSourceAccountByExternalApplicationId(externalApplicationId);
    }

    private void deleteApplicationByExternalApplicationId(String sourceAccount, String externalApplicationId) {
        log.info("Request to delete application by external application id {}, source account {}", externalApplicationId, sourceAccount);
        middlewareApplicationServiceApi.deleteApplicationByExternalApplicationId(sourceAccount, externalApplicationId);
    }

    @Override
    public ApplicationResponse updateDipApplicationById(String externalApplicationId, ApplicationRequest applicationRequest) {
        ApplicationResponse applicationResponse;
        var sourceAccount = getApplicationSourceAccountByExternalApplicationId(externalApplicationId);
        if (isAuthorisedToUpdateApplication(sourceAccount.getSourceAccount(), externalApplicationId, applicationRequest)) {
             applicationResponse = createDipApplication(applicationRequest);
             try {
                 deleteApplicationByExternalApplicationId(sourceAccount.getSourceAccount(), externalApplicationId);
             } catch (Exception e) {
                 log.error("********ALERT******** Unable to delete application external application id {}, source account {}, {} ********", externalApplicationId, sourceAccount, e.getMessage());
             }
        } else {
            throw new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE + " " + externalApplicationId);
        }
        return applicationResponse;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationResponse createDipApplication(ApplicationRequest applicationRequest) {
        log.debug("Create dip application applicationRequest {}", applicationRequest);
        applicationRequest.setSourceAccount(tokenService.retrieveSourceAccount());

        var appResponse =  middlewareApi.createDipApplication(applicationRequest);

        log.info("Finished calling mw to create dip application id {}", appResponse.getApplication().getExternalApplicationId());
        return appResponse;
    }


    @Override
    public Optional<ApplicationDecisionResponse> getApplicationByExternalApplicationId(String externalApplicationId) {
        var sourceAccount = getApplicationSourceAccountByExternalApplicationId(externalApplicationId);
        if (tokenService.retrieveSourceAccount().equals(sourceAccount.getSourceAccount())) {
            var applicationIdentifier = getApplicationIdByExternalApplicationId(externalApplicationId);
            return getApplicationById(applicationIdentifier.getId());
        } else {
            throw new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE + " " + externalApplicationId);
        }
    }

    private boolean isAuthorisedToUpdateApplication(String sourceAccount, String externalApplicationId, ApplicationRequest applicationRequest) {
        return (tokenService.retrieveSourceAccount().equals(sourceAccount)
                && externalApplicationId.equals(applicationRequest.getExternalApplicationId()));
    }

    private ApplicationIdentifier middlewareGetByExternalIdApiFallback(FeignException.FeignServerException e) { //NOSONAR
        defaultMiddlewareFallback(e);
        return ApplicationIdentifier.builder().build();
    }

    private ApplicationIdentifier middlewareGetByExternalIdApiFallback(feign.RetryableException e) { //NOSONAR
        defaultMiddlewareFallback(e);
        return ApplicationIdentifier.builder().build();
    }

    private Optional<ApplicationDecisionResponse> middlewareGetApiFallback(FeignException.FeignServerException e) { //NOSONAR
        defaultMiddlewareFallback(e);
        return Optional.empty();
    }

    private Optional<ApplicationDecisionResponse> middlewareGetApiFallback(feign.RetryableException e) { //NOSONAR
        defaultMiddlewareFallback(e);
        return Optional.empty();
    }

    private ApplicationResponse middlewareApiFallback(FeignException.FeignServerException e) { //NOSONAR
        defaultMiddlewareFallback(e);
        return ApplicationResponse.builder().build();
    }

    private ApplicationResponse middlewareApiFallback(feign.RetryableException e) { //NOSONAR
        defaultMiddlewareFallback(e);
        return ApplicationResponse.builder().build();
    }

    private void defaultMiddlewareFallback(Exception e) {
        log.error("Middleware is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
