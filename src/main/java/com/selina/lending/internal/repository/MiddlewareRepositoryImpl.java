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

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.dto.LendingConstants;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.Applicant;
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
        enrichApplicationRequest(applicationRequest, true);

        var appResponse = middlewareApi.createDipCCApplication(applicationRequest);

        log.info("Finished calling mw to create dipcc application [externalApplicationId={}]", appResponse.getApplication().getExternalApplicationId());
        return appResponse;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationResponse createDipApplication(ApplicationRequest applicationRequest) {
        log.debug("Create DIP application [applicationRequest={}]", applicationRequest);
        enrichApplicationRequest(applicationRequest, false);
        applicationRequest.setStageOverwrite(LendingConstants.STAGE_OVERWRITE);

        var appResponse = middlewareApi.createDipApplication(applicationRequest);

        log.info("Finished calling mw to create dip application [externalApplicationId={}]", appResponse.getApplication().getExternalApplicationId());
        return appResponse;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiSelectProductFallback")
    @Override
    public SelectProductResponse selectProduct(String id, String productCode) {
        log.info("Request to select product for [applicationId={}] [productCode={}]", id, productCode);
        return middlewareApi.selectProduct(id, productCode);
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareUpdateAppFallback")
    @Override
    public void patchApplication(String id, ApplicationRequest applicationRequest) {
        log.info("Update application for [applicationId={}]", id);

        applicationRequest.getApplicants().forEach(this::setIdentifier);
        middlewareApi.patchApplication(id, applicationRequest);
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareEsisApiFallback")
    @Override
    public Resource downloadEsisDocByAppId(String id) {
        log.info("Request to download esis doc by [applicationId={}]", id);
        return middlewareApi.downloadEsisByAppId(id);
    }

    private void enrichApplicationRequest(ApplicationRequest applicationRequest, boolean includeCreditCommitments) {
        applicationRequest.setSourceAccount(tokenService.retrieveSourceAccount());
        applicationRequest.setIncludeCreditCommitment(includeCreditCommitments);
        applicationRequest.setSource(LendingConstants.REQUEST_SOURCE);
        applicationRequest.setProductCode(LendingConstants.PRODUCT_CODE_ALL);
        applicationRequest.getApplicants().forEach(this::setIdentifier);
    }
    private void setIdentifier(Applicant applicant) {
        applicant.setIdentifier(Boolean.TRUE.equals(applicant.getPrimaryApplicant()) ?  0 : 1);
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

    private void middlewareUpdateAppFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }
    private Resource middlewareEsisApiFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private RemoteResourceProblemException remoteResourceProblemException(Exception e) {
        log.error("Middleware is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
