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

package com.selina.lending.internal.service.creditcommitments;

import org.springframework.stereotype.Service;

import com.selina.lending.internal.repository.CreditCommitmentsRepository;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;

import com.selina.lending.internal.service.AccessManagementService;
import com.selina.lending.httpclient.creditcommitments.dto.response.CreditCommitmentResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RetrieveCreditCommitmentsServiceImpl implements RetrieveCreditCommitmentsService {

    private final CreditCommitmentsRepository commitmentsRepository;
    private final MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;
    private final AccessManagementService accessManagementService;
    
    public RetrieveCreditCommitmentsServiceImpl(CreditCommitmentsRepository commitmentsRepository, MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository,
            AccessManagementService accessManagementService) {
        this.commitmentsRepository = commitmentsRepository;
        this.middlewareApplicationServiceRepository = middlewareApplicationServiceRepository;
        this.accessManagementService = accessManagementService;
    }
    
    @Override
    public CreditCommitmentResponse getCreditCommitments(String externalAppId) {
        var applicationIdentifier = middlewareApplicationServiceRepository.getAppIdByExternalId(externalAppId);
        accessManagementService.checkSourceAccountAccessPermitted(applicationIdentifier.getSourceAccount());
        log.info("Get credit commitments for [sourceAccount={}], [externalApplicationId={}]", applicationIdentifier.getSourceAccount(), externalAppId);
        return commitmentsRepository.getCreditCommitments(applicationIdentifier.getId());
    }
}
