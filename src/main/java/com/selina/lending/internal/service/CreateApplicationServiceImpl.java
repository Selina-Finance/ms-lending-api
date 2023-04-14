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

import com.selina.lending.internal.service.application.domain.Offer;
import com.selina.lending.internal.service.application.domain.quotecc.QuickQuoteCFRequest;
import com.selina.lending.internal.service.application.domain.quotecc.QuickQuoteCFResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.selina.lending.api.errors.custom.ConflictException;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class CreateApplicationServiceImpl implements CreateApplicationService {

    private static final String OFFER_DECISION_DECLINE = "Decline";
    private static final String APPLICATION_ALREADY_EXISTS_ERROR = "Application already exists";
    private final MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;
    private final MiddlewareRepository middlewareRepository;

    public CreateApplicationServiceImpl(MiddlewareRepository middlewareRepository,
            MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository) {
        this.middlewareRepository = middlewareRepository;
        this.middlewareApplicationServiceRepository = middlewareApplicationServiceRepository;
    }

    @Override
    public ApplicationResponse createDipCCApplication(ApplicationRequest applicationRequest) {
        checkApplicationExists(applicationRequest);
        return middlewareRepository.createDipCCApplication(applicationRequest);
    }

    @Override
    public ApplicationResponse createDipApplication(ApplicationRequest applicationRequest) {
        checkApplicationExists(applicationRequest);
        return middlewareRepository.createDipApplication(applicationRequest);
    }

    private void checkApplicationExists(ApplicationRequest applicationRequest) {
        try {
            var applicationIdentifier = middlewareApplicationServiceRepository.getAppIdByExternalId(applicationRequest.getExternalApplicationId());
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
