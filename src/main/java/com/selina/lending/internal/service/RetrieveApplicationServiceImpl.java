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

import com.selina.lending.internal.repository.GetApplicationRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.httpclient.middleware.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.internal.service.filter.RuleOutcomeFilter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RetrieveApplicationServiceImpl implements RetrieveApplicationService {
    private final GetApplicationRepository getApplicationRepository;
    private final MiddlewareRepository middlewareRepository;
    private final AccessManagementService accessManagementService;
    private final RuleOutcomeFilter ruleOutcomeFilter;

    public RetrieveApplicationServiceImpl(GetApplicationRepository getApplicationRepository, MiddlewareRepository middlewareRepository,
                                          AccessManagementService accessManagementService, RuleOutcomeFilter ruleOutcomeFilter) {
        this.getApplicationRepository = getApplicationRepository;
        this.middlewareRepository = middlewareRepository;
        this.accessManagementService = accessManagementService;
        this.ruleOutcomeFilter = ruleOutcomeFilter;
    }

    @Override
    public Optional<ApplicationDecisionResponse> getApplicationByExternalApplicationId(String externalApplicationId) {
        var applicationIdentifier = getApplicationRepository.getAppIdByExternalId(externalApplicationId);
        accessManagementService.checkSourceAccountAccessPermitted(applicationIdentifier.getSourceAccount());

        log.info("Get application by Id for [sourceAccount={}], [externalApplicationId={}]", applicationIdentifier.getSourceAccount(), externalApplicationId);
        Optional<ApplicationDecisionResponse> response = middlewareRepository.getApplicationById(applicationIdentifier.getId());
        response.ifPresent(this::filterRuleOutcomes);
        return response;
    }

    private void filterRuleOutcomes(ApplicationDecisionResponse response) {
        ruleOutcomeFilter.filterOfferRuleOutcomes(response.getDecision(), response.getApplicationType(), response.getOffers());
    }
}
