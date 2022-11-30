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
import com.selina.lending.internal.dto.LendingConstants;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.internal.service.application.domain.SelectProductResponse;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MiddlewareRepositoryImpl implements MiddlewareRepository {

    private final MiddlewareApi middlewareApi;

    private final TokenService tokenService;

    public MiddlewareRepositoryImpl(MiddlewareApi middlewareApi, TokenService tokenService) {
        this.middlewareApi = middlewareApi;
        this.tokenService = tokenService;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareGetApiFallback")
    @Override
    public Optional<ApplicationDecisionResponse> getApplicationById(String id) {
        log.info("Request to get application by [applicationId={}]", id);
        return Optional.of(middlewareApi.getApplicationById(id));
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationResponse createDipCCApplication(ApplicationRequest applicationRequest) {
        log.debug("Create DIP with Credit Commitments application [applicationRequest={}]", applicationRequest);
        enrichApplicationRequest(applicationRequest);

        var appResponse =  middlewareApi.createDipCCApplication(applicationRequest);

        log.info("Finished calling mw to create dipcc application [externalApplicationId={}]", appResponse.getApplication().getExternalApplicationId());
        return appResponse;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationResponse createDipApplication(ApplicationRequest applicationRequest) {
        log.debug("Create DIP application [applicationRequest={}]", applicationRequest);
        enrichApplicationRequest(applicationRequest);

        var appResponse =  middlewareApi.createDipApplication(applicationRequest);

        log.info("Finished calling mw to create dip application [externalApplicationId={}]", appResponse.getApplication().getExternalApplicationId());
        return appResponse;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiSelectProductFallback")
    @Override
    public SelectProductResponse selectProduct(String id, String productCode) {
        log.info("Request to select product for [applicationId={}] [productCode={}]", id, productCode);
        return middlewareApi.selectProduct(id, productCode);
    }

    private void enrichApplicationRequest(ApplicationRequest applicationRequest) {
        applicationRequest.setSourceAccount(tokenService.retrieveSourceAccount());
        applicationRequest.setSource(LendingConstants.REQUEST_SOURCE);
        applicationRequest.setProductCode(LendingConstants.PRODUCT_CODE_ALL);
    }

    private SelectProductResponse middlewareApiSelectProductFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private Optional<ApplicationDecisionResponse> middlewareGetApiFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private ApplicationResponse middlewareApiFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private RemoteResourceProblemException remoteResourceProblemException(Exception e) {
        log.error("Middleware is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
