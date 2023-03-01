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
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.AccessManagementService;
import com.selina.lending.internal.service.application.domain.creditcommitments.UpdateCreditCommitmentsRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UpdateCreditCommitmentsServiceImpl implements UpdateCreditCommitmentsService {

    private final MiddlewareApplicationServiceRepository applicationRepository;
    private final MiddlewareRepository middlewareRepository;

    private final CreditCommitmentsRepository commitmentsRepository;
    private final AccessManagementService accessManagementService;

    public UpdateCreditCommitmentsServiceImpl(CreditCommitmentsRepository commitmentsRepository,
            AccessManagementService accessManagementService,  MiddlewareApplicationServiceRepository applicationRepository,
            MiddlewareRepository middlewareRepository) {
        this.commitmentsRepository = commitmentsRepository;
        this.accessManagementService = accessManagementService;
        this.applicationRepository = applicationRepository;
        this.middlewareRepository = middlewareRepository;
    }

    @Override
    public void updateCreditCommitments(String externalId, UpdateCreditCommitmentsRequest request) {
        var applicationIdentifier = applicationRepository.getAppIdByExternalId(externalId);
        accessManagementService.checkSourceAccountAccessPermitted(applicationIdentifier.getSourceAccount());
        log.info("Update credit commitments for [sourceAccount={}], [externalApplicationId={}]", applicationIdentifier.getSourceAccount(), externalId);
        commitmentsRepository.patchCreditCommitments(applicationIdentifier.getId(), request);
        middlewareRepository.checkAffordability(applicationIdentifier.getId());
    }
}
