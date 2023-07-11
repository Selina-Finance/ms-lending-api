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

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.httpclient.creditcommitments.CreditCommitmentsApi;
import com.selina.lending.httpclient.creditcommitments.dto.response.CreditCommitmentResponse;
import com.selina.lending.httpclient.creditcommitments.dto.response.PatchCreditCommitmentResponse;
import com.selina.lending.httpclient.creditcommitments.dto.request.UpdateCreditCommitmentsRequest;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreditCommitmentsRepositoryImpl implements CreditCommitmentsRepository {

    private final CreditCommitmentsApi commitmentsApi;

    public CreditCommitmentsRepositoryImpl(CreditCommitmentsApi commitmentsApi) {
        this.commitmentsApi = commitmentsApi;
    }

    @Retry(name = "middleware-application-service-retry", fallbackMethod = "patchCCFallback")
    @Override
    public PatchCreditCommitmentResponse patchCreditCommitments(String id, UpdateCreditCommitmentsRequest request) {
        log.info("Request to patch credit commitments by [applicationId={}]", id);
        return commitmentsApi.patchCreditCommitments(id, request);
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "getCCFallback")
    @Override
    public CreditCommitmentResponse getCreditCommitments(String id) {
        log.info("Request to get credit commitments by [applicationId={}]", id);
        return commitmentsApi.getCreditCommitments(id);
    }

    private PatchCreditCommitmentResponse patchCCFallback(FeignException.FeignServerException e) { //NOSONAR
        log.error("Patch CreditCommitments service is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }

    private CreditCommitmentResponse getCCFallback(FeignException.FeignServerException e) { //NOSONAR
        log.error("Get CreditCommitments service is unavailable. {} {}", e.getCause(), e.getMessage());
        throw new RemoteResourceProblemException();
    }
}
