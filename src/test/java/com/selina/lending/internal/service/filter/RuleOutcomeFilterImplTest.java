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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.selina.lending.internal.dto.LendingConstants.ACCEPT_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.DECLINE_DECISION;
import com.selina.lending.internal.service.application.domain.RuleOutcome;

@ExtendWith(MockitoExtension.class)
class RuleOutcomeFilterImplTest {

    @InjectMocks
    private RuleOutcomeFilterImpl ruleOutcomeFilter;

    @Test
    void shouldFilterRuleOutcomesToOnlyIncludeDecline() {
        //Given
        var ruleOutcomes = List.of(buildRuleOutcome(ACCEPT_DECISION), buildRuleOutcome(DECLINE_DECISION));

        //When
        var filteredList = ruleOutcomeFilter.filterRuleOutcomes(ruleOutcomes);

        //Then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList.get(0).getOutcome()).isEqualTo(DECLINE_DECISION);
    }

    @Test
    void shouldNotFilterRuleOutcomesWhenRuleOutcomesIsNull() {
        //Given
        //When
        var filteredList = ruleOutcomeFilter.filterRuleOutcomes(null);

        //Then
        assertThat(filteredList).isNull();
    }

    @Test
    void shouldNotFilterRuleOutcomesWhenRuleOutcomesIsEmpty() {
        //Given
        List<RuleOutcome> ruleOutcomes = new ArrayList<>();

        //When
        var filteredList = ruleOutcomeFilter.filterRuleOutcomes(ruleOutcomes);

        //Then
        assertThat(filteredList).isNull();
    }

    private RuleOutcome buildRuleOutcome(String outcome) {
        return RuleOutcome.builder().outcome(outcome).build();
    }
}