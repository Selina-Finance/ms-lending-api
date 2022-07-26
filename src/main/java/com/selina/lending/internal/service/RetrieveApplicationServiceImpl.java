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

@Service
public class RetrieveApplicationServiceImpl implements RetrieveApplicationService {
    private final MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;
    private final MiddlewareRepository middlewareRepository;
    private final AccessManagementService accessManagementService;

    public RetrieveApplicationServiceImpl(MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository, MiddlewareRepository middlewareRepository,
            AccessManagementService accessManagementService) {
        this.middlewareApplicationServiceRepository = middlewareApplicationServiceRepository;
        this.middlewareRepository = middlewareRepository;
        this.accessManagementService = accessManagementService;
    }

    @Override
    public Optional<ApplicationDecisionResponse> getApplicationByExternalApplicationId(String externalApplicationId) {
        var applicationIdentifier = middlewareApplicationServiceRepository.getAppIdByExternalId(externalApplicationId);
        accessManagementService.checkSourceAccountAccessPermitted(applicationIdentifier.getSourceAccount());
        return middlewareRepository.getApplicationById(applicationIdentifier.getId());
    }
}
