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

package com.selina.lending.service;

import com.selina.lending.httpclient.middleware.dto.product.response.SelectProductResponse;
import com.selina.lending.repository.GetApplicationRepository;
import com.selina.lending.repository.MiddlewareRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final GetApplicationRepository getApplicationRepository;
    private final MiddlewareRepository middlewareRepository;

    private final AccessManagementService accessManagementService;

    public ProductServiceImpl(MiddlewareRepository middlewareRepository, GetApplicationRepository getApplicationRepository, AccessManagementService accessManagementService) {
        this.middlewareRepository = middlewareRepository;
        this.getApplicationRepository = getApplicationRepository;
        this.accessManagementService = accessManagementService;
    }

    @Override
    public SelectProductResponse selectProductOffer(String externalApplicationId, String productCode) {
        var applicationIdentifier = getApplicationRepository.getAppIdByExternalId(externalApplicationId);
        accessManagementService.checkSourceAccountAccessPermitted(applicationIdentifier.getSourceAccount());

        log.info("Select product for [externalApplicationId={}] [applicationId={}] [productCode={}] [sourceAccount={}]", externalApplicationId, applicationIdentifier.getId(), productCode, applicationIdentifier.getSourceAccount());
        return middlewareRepository.selectProduct(applicationIdentifier.getId(), productCode);
    }
}
