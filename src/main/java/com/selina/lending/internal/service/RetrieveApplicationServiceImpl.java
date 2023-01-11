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

package com.selina.lending.internal.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.creditcommitments.CreditCommitmentResponse;
import com.selina.lending.internal.service.creditcommitments.RetrieveCreditCommitmentsService;

@Service
public class RetrieveApplicationServiceImpl implements RetrieveApplicationService {
    private final MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;
    private final MiddlewareRepository middlewareRepository;
    private final AccessManagementService accessManagementService;
    private final RetrieveCreditCommitmentsService retrieveCreditCommitmentsService;

    public RetrieveApplicationServiceImpl(MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository, MiddlewareRepository middlewareRepository,
            AccessManagementService accessManagementService, RetrieveCreditCommitmentsService retrieveCreditCommitmentsService) {
        this.middlewareApplicationServiceRepository = middlewareApplicationServiceRepository;
        this.middlewareRepository = middlewareRepository;
        this.accessManagementService = accessManagementService;
        this.retrieveCreditCommitmentsService = retrieveCreditCommitmentsService;
    }

    @Override
    public Optional<ApplicationDecisionResponse> getApplicationByExternalApplicationId(String externalApplicationId) {
        var applicationIdentifier = middlewareApplicationServiceRepository.getAppIdByExternalId(externalApplicationId);
        accessManagementService.checkSourceAccountAccessPermitted(applicationIdentifier.getSourceAccount());

        Optional<ApplicationDecisionResponse> response = middlewareRepository.getApplicationById(applicationIdentifier.getId());
        //Get the commitments but use try catch
        try {
            if (response.isPresent()) {
                CreditCommitmentResponse creditCommitments = retrieveCreditCommitmentsService.getCreditCommitments(
                        applicationIdentifier.getId());
                response.get().setCreditCommitment(creditCommitments.getCreditCommitment());
            }
        } catch (Exception e) {
            //log error
        }
        return response;
    }
}
