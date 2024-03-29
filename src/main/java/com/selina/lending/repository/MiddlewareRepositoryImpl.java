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

package com.selina.lending.repository;

import com.selina.lending.exception.RemoteResourceProblemException;
import com.selina.lending.httpclient.middleware.MiddlewareApi;
import com.selina.lending.httpclient.middleware.MiddlewareQQApi;
import com.selina.lending.httpclient.middleware.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import com.selina.lending.httpclient.middleware.dto.dip.response.ApplicationResponse;
import com.selina.lending.httpclient.middleware.dto.product.response.SelectProductResponse;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.response.QuickQuoteCFResponse;
import com.selina.lending.service.enricher.MiddlewareRequestEnricher;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MiddlewareRepositoryImpl implements MiddlewareRepository {

    private final MiddlewareApi middlewareApi;
    private final MiddlewareQQApi middlewareQQApi;
    private final MiddlewareRequestEnricher middlewareRequestEnricher;

    public MiddlewareRepositoryImpl(MiddlewareApi middlewareApi,
                                    MiddlewareQQApi middlewareQQApi,
                                    MiddlewareRequestEnricher middlewareRequestEnricher) {
        this.middlewareApi = middlewareApi;
        this.middlewareQQApi = middlewareQQApi;
        this.middlewareRequestEnricher = middlewareRequestEnricher;
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
        middlewareRequestEnricher.enrichCreateDipCCApplicationRequest(applicationRequest);

        var appResponse = middlewareApi.createDipCCApplication(applicationRequest);

        log.info("Finished calling mw to create dipcc application [externalApplicationId={}]", appResponse.getApplication().getExternalApplicationId());
        return appResponse;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationResponse createDipApplication(ApplicationRequest applicationRequest) {
        log.debug("Create DIP application [applicationRequest={}]", applicationRequest);
        middlewareRequestEnricher.enrichCreateDipApplicationRequest(applicationRequest);

        var appResponse = middlewareApi.createDipApplication(applicationRequest);

        log.info("Finished calling mw to create dip application [externalApplicationId={}]", appResponse.getApplication().getExternalApplicationId());
        return appResponse;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiCreateQuickQuoteCFFallback")
    @Override
    public QuickQuoteCFResponse createQuickQuoteCFApplication(QuickQuoteCFRequest applicationRequest) {
        log.debug("Create QQ with Credit File application [applicationRequest={}]", applicationRequest);
        middlewareRequestEnricher.enrichCreateQuickQuoteCFRequest(applicationRequest);

        var appResponse = middlewareApi.createQuickQuoteCFApplication(applicationRequest);

        log.info("Finished calling mw to create qqcf application [externalApplicationId={}]", appResponse.getExternalApplicationId());
        return appResponse;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiSelectProductFallback")
    @Override
    public SelectProductResponse selectProduct(String id, String productCode) {
        log.info("Request to select product for [applicationId={}] [productCode={}]", id, productCode);
        return middlewareApi.selectProduct(id, productCode);
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareUpdateDipAppFallback")
    @Override
    public void patchDipApplication(String id, ApplicationRequest applicationRequest) {
        log.info("Update DIP application for [applicationId={}]", id);
        middlewareRequestEnricher.enrichPatchApplicationRequest(applicationRequest);
        middlewareApi.patchDipApplication(id, applicationRequest);
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareUpdateDipCCAppFallback")
    @Override
    public void patchDipCCApplication(String id, ApplicationRequest applicationRequest) {
        log.info("Update DIP CC application for [applicationId={}]", id);
        middlewareRequestEnricher.enrichPatchApplicationRequest(applicationRequest);
        middlewareApi.patchDipCCApplication(id, applicationRequest);
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationResponse checkAffordability(String id) {
        log.info("Check affordability for [applicationId={}]", id);

        var response = middlewareApi.checkAffordability(id);

        log.info("Affordability check completed for [applicationId={}]", id);

        return response;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareEsisApiFallback")
    @Override
    public Resource downloadEsisDocByAppId(String id) {
        log.info("Request to download esis doc by [applicationId={}]", id);
        return middlewareApi.downloadEsisByAppId(id);
    }

    @Retry(name = "middleware-qq-api-retry", fallbackMethod = "middlewareApiCreateQuickQuoteFallback")
    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiCreateQuickQuoteFallback")
    public void createQuickQuoteApplication(QuickQuoteRequest applicationRequest){
        log.info("Create QQ application [externalApplicationId={}]", applicationRequest.getExternalApplicationId());
        middlewareQQApi.createQuickQuoteApplication(applicationRequest);
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

    private QuickQuoteCFResponse middlewareApiCreateQuickQuoteCFFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private void middlewareUpdateDipAppFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private void middlewareUpdateDipCCAppFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private Resource middlewareEsisApiFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private void middlewareApiCreateQuickQuoteFallback(RetryableException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private void middlewareApiCreateQuickQuoteFallback(CallNotPermittedException e) { //NOSONAR
        throw remoteResourceProblemException(e);
    }

    private RemoteResourceProblemException remoteResourceProblemException(Exception e) {
        log.error("Middleware is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
