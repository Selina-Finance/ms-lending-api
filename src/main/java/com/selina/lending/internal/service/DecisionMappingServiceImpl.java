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

import static com.selina.lending.internal.dto.LendingConstants.ACCEPT_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.ACCEPT_STATUS;
import static com.selina.lending.internal.dto.LendingConstants.REFER_DECISION;

import java.util.List;

import org.springframework.stereotype.Service;

import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.internal.service.application.domain.Offer;

@Service
public class DecisionMappingServiceImpl implements DecisionMappingService {
    @Override
    public void mapDecision(ApplicationResponse applicationResponse) {
        //TODO check the ApplicationResponse decision and map from Refer to Accept
        // if (REFER_DECISION.equalsIgnoreCase(applicationResponse.getDecision()) {
        // applicationResponse.setDecision(ACCEPT_DECISION)
        // and then mapOffers (call below)

        mapOffers(applicationResponse.getApplication().getOffers());
    }

    private void mapOffers(List<Offer> offers) {
        offers.forEach(offer -> {
            if (REFER_DECISION.equalsIgnoreCase(offer.getDecision())) {
                offer.setDecision(ACCEPT_DECISION);
            }
        });
    }

    @Override
    public void mapDecision(ApplicationDecisionResponse decisionResponse) {
        if (REFER_DECISION.equalsIgnoreCase(decisionResponse.getDecision())) {
            decisionResponse.setDecision(ACCEPT_DECISION);
        }
        if (decisionResponse.getStatus().contains(REFER_DECISION)) {
            decisionResponse.setStatus(ACCEPT_STATUS);
        }

        mapOffers(decisionResponse.getOffers());
    }
}
