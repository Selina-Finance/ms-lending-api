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
    public void updateDipCCApplication(String externalApplicationId, ApplicationRequest applicationRequest) {
        patchApplication(externalApplicationId, applicationRequest);
    }

    @Override
    public void updateDipApplication(String externalApplicationId, ApplicationRequest applicationRequest) {
        patchApplication(externalApplicationId, applicationRequest);
    }

    private void patchApplication(String externalApplicationId, ApplicationRequest applicationRequest) {
        var applicationIdentifier = middlewareApplicationServiceRepository.getAppIdByExternalId(externalApplicationId);
        if (isAuthorisedToUpdateApplication(applicationIdentifier.getSourceAccount(), externalApplicationId,
                applicationRequest)) {
            middlewareRepository.patchApplication(applicationIdentifier.getId(), applicationRequest);
        } else {
            throw new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE + " " + externalApplicationId);
        }
    }

    private boolean isAuthorisedToUpdateApplication(String sourceAccount, String externalApplicationId, ApplicationRequest applicationRequest) {
        return (accessManagementService.isSourceAccountAccessAllowed(sourceAccount)
                && externalApplicationId.equals(applicationRequest.getExternalApplicationId()));
    }
}
