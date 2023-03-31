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

import static org.assertj.core.api.Assertions.assertThat;

import static com.selina.lending.internal.dto.LendingConstants.ACCEPT_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.DECLINE_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.REFER_DECISION;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.internal.service.application.domain.Offer;
import com.selina.lending.internal.service.application.domain.RuleOutcome;

@ExtendWith(MockitoExtension.class)
class RuleOutcomeFilterImplTest {

    @InjectMocks
    private RuleOutcomeFilterImpl ruleOutcomeFilter;

    @Test
    void shouldFilterRuleOutcomesToOnlyIncludeDecline() {
        //Given
        var ruleOutcomes = List.of(buildRuleOutcome(ACCEPT_DECISION), buildRuleOutcome(DECLINE_DECISION));
        var offers = List.of(Offer.builder().ruleOutcomes(ruleOutcomes).build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(DECLINE_DECISION, offers);

        //Then
        assertThat(offers).hasSize(1);
        assertThat(offers.get(0).getRuleOutcomes()).hasSize(1);
        assertThat(offers.get(0).getRuleOutcomes().get(0).getOutcome()).isEqualTo(DECLINE_DECISION);
    }

    @Test
    void shouldRemoveRuleOutcomesIfDecisionIsAccept() {
        //Given
        var ruleOutcomes = List.of(buildRuleOutcome(ACCEPT_DECISION), buildRuleOutcome(REFER_DECISION),
                buildRuleOutcome(DECLINE_DECISION));
        var offers = List.of(Offer.builder().ruleOutcomes(ruleOutcomes).build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(ACCEPT_DECISION, offers);

        //Then
        assertThat(offers).hasSize(1);
        assertThat(offers.get(0).getRuleOutcomes()).isNull();
    }

    @Test
    void shouldNotFilterRuleOutcomesWhenRuleOutcomesIsNull() {
        //Given
        var offers = List.of(Offer.builder().build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(ACCEPT_DECISION, offers);

        //Then
        assertThat(offers.get(0).getRuleOutcomes()).isNull();
    }

    @Test
    void shouldNotFilterRuleOutcomesWhenRuleOutcomesIsEmpty() {
        //Given
        List<RuleOutcome> ruleOutcomes = new ArrayList<>();
        var offers = List.of(Offer.builder().ruleOutcomes(ruleOutcomes).build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(ACCEPT_DECISION, offers);

        //Then
        assertThat(offers.get(0).getRuleOutcomes()).isNull();
    }

    private RuleOutcome buildRuleOutcome(String outcome) {
        return RuleOutcome.builder().outcome(outcome).build();
    }
}