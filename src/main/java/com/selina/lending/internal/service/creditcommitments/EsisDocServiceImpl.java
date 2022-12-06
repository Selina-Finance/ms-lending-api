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

import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.AccessManagementService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class EsisDocServiceImpl implements EsisDocService {

    private final MiddlewareApplicationServiceRepository applicationRepository;
    private final AccessManagementService accessManagementService;
    private final MiddlewareRepository middlewareRepository;

    public EsisDocServiceImpl(MiddlewareApplicationServiceRepository applicationRepository,
                              AccessManagementService accessManagementService,
                              MiddlewareRepository middlewareRepository) {
        this.applicationRepository = applicationRepository;
        this.accessManagementService = accessManagementService;
        this.middlewareRepository = middlewareRepository;
    }

    @Override
    public Resource getByExternalAppId(String externalAppId) {
        var identifier = applicationRepository.getAppIdByExternalId(externalAppId);
        accessManagementService.checkSourceAccountAccessPermitted(identifier.getSourceAccount());

        return middlewareRepository.downloadEsisDocByAppId(identifier.getId());
    }
}
