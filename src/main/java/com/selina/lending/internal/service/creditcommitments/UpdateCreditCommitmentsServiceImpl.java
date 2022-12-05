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

import com.selina.lending.internal.dto.creditcommitments.UpdateCreditCommitmentsRequest;
import com.selina.lending.internal.repository.CreditCommitmentsRepository;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.service.AccessManagementService;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import org.springframework.stereotype.Service;

@Service
public class UpdateCreditCommitmentsServiceImpl implements UpdateCreditCommitmentsService {

    private final MiddlewareApplicationServiceRepository applicationRepository;
    private final CreditCommitmentsRepository commitmentsRepository;
    private final AccessManagementService accessManagementService;

    public UpdateCreditCommitmentsServiceImpl(MiddlewareApplicationServiceRepository applicationRepository, CreditCommitmentsRepository commitmentsRepository, AccessManagementService accessManagementService) {
        this.applicationRepository = applicationRepository;
        this.commitmentsRepository = commitmentsRepository;
        this.accessManagementService = accessManagementService;
    }

    @Override
    public ApplicationResponse patchCreditCommitments(String externalId, UpdateCreditCommitmentsRequest request) {
        var applicationIdentifier = applicationRepository.getAppIdByExternalId(externalId);
        accessManagementService.checkSourceAccountAccessPermitted(applicationIdentifier.getSourceAccount());
        commitmentsRepository.patchCreditCommitments(applicationIdentifier.getId(), request);
        return applicationRepository.runDecisioningByAppId(applicationIdentifier.getId());
    }
}
