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

import org.springframework.stereotype.Service;

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

@Service
public class UpdateApplicationServiceImpl implements UpdateApplicationService {
    private final MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;
    private final MiddlewareRepository middlewareRepository;

    private final AccessManagementService accessManagementService;

    public UpdateApplicationServiceImpl(MiddlewareRepository middlewareRepository, MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository,
            AccessManagementService accessManagementService) {
        this.middlewareRepository = middlewareRepository;
        this.middlewareApplicationServiceRepository = middlewareApplicationServiceRepository;
        this.accessManagementService = accessManagementService;
    }

    @Override
    public ApplicationResponse updateDipCCApplication(String externalApplicationId, ApplicationRequest applicationRequest) {
        ApplicationResponse applicationResponse;
        var sourceAccount = middlewareApplicationServiceRepository.getApplicationSourceAccountByExternalApplicationId(externalApplicationId);
        if (isAuthorisedToUpdateApplication(sourceAccount.getSourceAccount(), externalApplicationId, applicationRequest)) {
            applicationResponse = middlewareRepository.createDipCCApplication(applicationRequest);
            middlewareApplicationServiceRepository.deleteApplicationByExternalApplicationId(sourceAccount.getSourceAccount(), externalApplicationId);
        } else {
            throw new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE + " " + externalApplicationId);
        }
        return applicationResponse;
    }

    @Override
    public ApplicationResponse updateDipApplication(String externalApplicationId, ApplicationRequest applicationRequest) {
        ApplicationResponse applicationResponse;
        var sourceAccount = middlewareApplicationServiceRepository.getApplicationSourceAccountByExternalApplicationId(externalApplicationId);
        if (isAuthorisedToUpdateApplication(sourceAccount.getSourceAccount(), externalApplicationId, applicationRequest)) {
            applicationResponse = middlewareRepository.createDipApplication(applicationRequest);
            middlewareApplicationServiceRepository.deleteApplicationByExternalApplicationId(sourceAccount.getSourceAccount(), externalApplicationId);
        } else {
            throw new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE + " " + externalApplicationId);
        }
        return applicationResponse;
    }

    private boolean isAuthorisedToUpdateApplication(String sourceAccount, String externalApplicationId, ApplicationRequest applicationRequest) {
        return (accessManagementService.isSourceAccountAccessAllowed(sourceAccount)
                && externalApplicationId.equals(applicationRequest.getExternalApplicationId()));
    }
}
