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

import org.springframework.stereotype.Service;

import com.selina.lending.exception.RemoteResourceProblemException;
import com.selina.lending.httpclient.getapplication.GetApplicationApi;
import com.selina.lending.httpclient.getapplication.dto.response.ApplicationIdentifier;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GetApplicationRepositoryImpl implements GetApplicationRepository {
    private final GetApplicationApi getApplicationApi;

    public GetApplicationRepositoryImpl(GetApplicationApi getApplicationApi) {
        this.getApplicationApi = getApplicationApi;
    }

    @CircuitBreaker(name = "middleware-application-service-cb", fallbackMethod = "middlewareGetByExternalIdApiFallback")
    @Override
    public ApplicationIdentifier getAppIdByExternalId(String externalAppId) {
        log.info("Request to get application Id by [externalApplicationId={}]", externalAppId);
        return getApplicationApi.getApplicationIdByExternalApplicationId(externalAppId);
    }

    private ApplicationIdentifier middlewareGetByExternalIdApiFallback(CallNotPermittedException e) { //NOSONAR
       throw remoteResourceProblemException(e);
    }

    private RemoteResourceProblemException remoteResourceProblemException(Exception e) {
        log.error("Middleware application service is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
