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

import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.SelectProductResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;
    private final MiddlewareRepository middlewareRepository;

    private final AccessManagementService accessManagementService;

    public ProductServiceImpl(MiddlewareRepository middlewareRepository, MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository, AccessManagementService accessManagementService) {
        this.middlewareRepository = middlewareRepository;
        this.middlewareApplicationServiceRepository = middlewareApplicationServiceRepository;
        this.accessManagementService = accessManagementService;
    }

    @Override
    public SelectProductResponse selectProductOffer(String externalApplicationId, String productCode) {
        var sourceAccount = middlewareApplicationServiceRepository.getAppSourceAccountByExternalAppId(externalApplicationId);
        accessManagementService.checkSourceAccountAccessPermitted(sourceAccount.getSourceAccount());
        var applicationIdentifier = middlewareApplicationServiceRepository.getAppIdByExternalId(externalApplicationId);

        log.info("Select product for [externalApplicationId={}] [applicationId={}] [productCode={}] [sourceAccount={}]", externalApplicationId, applicationIdentifier.getId(), productCode, sourceAccount.getSourceAccount());
        return middlewareRepository.selectProduct(applicationIdentifier.getId(), productCode);
    }
}
