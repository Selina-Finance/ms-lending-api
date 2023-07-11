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

import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.response.QuickQuoteCFResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.selina.lending.api.errors.custom.ConflictException;
import com.selina.lending.internal.repository.GetApplicationRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import com.selina.lending.httpclient.middleware.dto.dip.response.ApplicationResponse;
import com.selina.lending.internal.service.filter.RuleOutcomeFilter;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class CreateApplicationServiceImpl implements CreateApplicationService {

    private static final String OFFER_DECISION_DECLINE = "Decline";
    private static final String APPLICATION_ALREADY_EXISTS_ERROR = "Application already exists";
    private final GetApplicationRepository getApplicationRepository;
    private final MiddlewareRepository middlewareRepository;
    private final RuleOutcomeFilter ruleOutcomeFilter;

    public CreateApplicationServiceImpl(MiddlewareRepository middlewareRepository,
                                        GetApplicationRepository getApplicationRepository, RuleOutcomeFilter ruleOutcomeFilter) {
        this.middlewareRepository = middlewareRepository;
        this.getApplicationRepository = getApplicationRepository;
        this.ruleOutcomeFilter = ruleOutcomeFilter;
    }

    @Override
    public ApplicationResponse createDipCCApplication(ApplicationRequest applicationRequest) {
        checkApplicationExists(applicationRequest);
        ApplicationResponse applicationResponse = middlewareRepository.createDipCCApplication(applicationRequest);
        ruleOutcomeFilter.filterOfferRuleOutcomes(applicationResponse.getApplication().getDecision(), applicationResponse.getApplicationType(),
                applicationResponse.getApplication()
                        .getOffers());
        return applicationResponse;
    }

    @Override
    public ApplicationResponse createDipApplication(ApplicationRequest applicationRequest) {
        checkApplicationExists(applicationRequest);
        ApplicationResponse applicationResponse = middlewareRepository.createDipApplication(applicationRequest);
        ruleOutcomeFilter.filterOfferRuleOutcomes(applicationResponse.getApplication().getDecision(), applicationResponse.getApplicationType(),
                applicationResponse.getApplication()
                        .getOffers());
        return applicationResponse;
    }

    private void checkApplicationExists(ApplicationRequest applicationRequest) {
        try {
            var applicationIdentifier = getApplicationRepository.getAppIdByExternalId(applicationRequest.getExternalApplicationId());
            log.info("Check if application already exists [externalApplicationId={}], [sourceAccount={}]", applicationRequest.getExternalApplicationId(), applicationIdentifier.getSourceAccount());
            if (StringUtils.isNotEmpty(applicationIdentifier.getId())) {
                throw new ConflictException(APPLICATION_ALREADY_EXISTS_ERROR + " " + applicationRequest.getExternalApplicationId());
            }
        } catch (FeignException.NotFound ignore) {
            //application does not exist, so we can safely ignore this exception and create the application
        }
    }

    @Override
    public QuickQuoteCFResponse createQuickQuoteCFApplication(QuickQuoteCFRequest applicationRequest) {
        var quickQuoteCFResponse = middlewareRepository.createQuickQuoteCFApplication(applicationRequest);
        quickQuoteCFResponse.setOffers(filterOutDeclinedOffers(quickQuoteCFResponse.getOffers()));
        return quickQuoteCFResponse;
    }

    private List<Offer> filterOutDeclinedOffers(List<Offer> offers) {
        return offers.stream()
                .filter(offer -> !OFFER_DECISION_DECLINE.equalsIgnoreCase(offer.getDecision()))
                .toList();
    }
}
