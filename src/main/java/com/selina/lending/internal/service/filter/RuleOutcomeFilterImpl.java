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

package com.selina.lending.internal.service.filter;

import static com.selina.lending.internal.dto.LendingConstants.DIP_APPLICATION_TYPE;
import static com.selina.lending.internal.dto.LendingConstants.DECLINE_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.REFER_DECISION;

import java.util.List;

import org.springframework.stereotype.Component;

import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.middleware.dto.common.RuleOutcome;

@Component
public class RuleOutcomeFilterImpl implements RuleOutcomeFilter {

    @Override
    public void filterOfferRuleOutcomes(String decision, String applicationType, List<Offer> offers) {
        if (DIP_APPLICATION_TYPE.equalsIgnoreCase(applicationType)) {
            filterRuleOutcomesForDIP(decision, offers);
        } else {
            removeRuleOutcomes(offers);
        }
    }

    private void filterRuleOutcomesForDIP(String decision, List<Offer> offers) {
        if (DECLINE_DECISION.equalsIgnoreCase(decision) || REFER_DECISION.equalsIgnoreCase(decision)) {
            offers.forEach(offer -> offer.setRuleOutcomes(filterRuleOutcomes(offer.getRuleOutcomes())));
        } else {
            removeRuleOutcomes(offers);
        }
    }

    private List<RuleOutcome> filterRuleOutcomes(List<RuleOutcome> ruleOutcomeList) {
        return ruleOutcomeList != null && !ruleOutcomeList.isEmpty() ?
                ruleOutcomeList.stream().filter(this::filterOutcome).toList() :
                null;
    }

    private void removeRuleOutcomes (List<Offer> offers) {
        if (offers != null && !offers.isEmpty()) {
            offers.forEach(offer -> offer.setRuleOutcomes(null));
        }
    }

    private boolean filterOutcome(RuleOutcome outcome) {
        return DECLINE_DECISION.equalsIgnoreCase(outcome.getOutcome()) || REFER_DECISION.equalsIgnoreCase(outcome.getOutcome());
    }
}
