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

import static com.selina.lending.internal.dto.LendingConstants.DIP_APPLICATION_TYPE;
import static com.selina.lending.internal.dto.LendingConstants.ACCEPT_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.DECLINE_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.REFER_DECISION;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.middleware.dto.common.RuleOutcome;

@ExtendWith(MockitoExtension.class)
class RuleOutcomeFilterImplTest {

    @InjectMocks
    private RuleOutcomeFilterImpl ruleOutcomeFilter;

    @Test
    void shouldFilterRuleOutcomesForDecisionDeclineToOnlyIncludeDeclineRuleOutcomes() {
        //Given
        var ruleOutcomes = List.of(buildRuleOutcome(ACCEPT_DECISION), buildRuleOutcome(DECLINE_DECISION), buildRuleOutcome(
                DECLINE_DECISION));
        var offers = List.of(Offer.builder().ruleOutcomes(ruleOutcomes).build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(DECLINE_DECISION, DIP_APPLICATION_TYPE, offers);

        //Then
        assertThat(offers).hasSize(offers.size());
        assertThat(offers.get(0).getRuleOutcomes()).hasSize(2);
        assertThat(offers.get(0).getRuleOutcomes().get(0).getOutcome()).isEqualTo(DECLINE_DECISION);
        assertThat(offers.get(0).getRuleOutcomes().get(1).getOutcome()).isEqualTo(DECLINE_DECISION);
    }

    @Test
    void shouldFilterRuleOutcomesForDecisionReferToOnlyIncludeReferRuleOutcomes() {
        //Given
        var ruleOutcomes = List.of(buildRuleOutcome(ACCEPT_DECISION), buildRuleOutcome(REFER_DECISION), buildRuleOutcome(
                REFER_DECISION));
        var offers = List.of(Offer.builder().ruleOutcomes(ruleOutcomes).build(), Offer.builder().ruleOutcomes(ruleOutcomes).build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(REFER_DECISION, DIP_APPLICATION_TYPE, offers);

        //Then
        assertThat(offers).hasSize(offers.size());
        assertThat(offers.get(0).getRuleOutcomes()).hasSize(2);
        assertThat(offers.get(0).getRuleOutcomes().get(0).getOutcome()).isEqualTo(REFER_DECISION);
        assertThat(offers.get(0).getRuleOutcomes().get(1).getOutcome()).isEqualTo(REFER_DECISION);

        assertThat(offers.get(1).getRuleOutcomes()).hasSize(2);
        assertThat(offers.get(1).getRuleOutcomes().get(0).getOutcome()).isEqualTo(REFER_DECISION);
        assertThat(offers.get(1).getRuleOutcomes().get(1).getOutcome()).isEqualTo(REFER_DECISION);
    }

    @Test
    void shouldRemoveRuleOutcomesIfDecisionIsAccept() {
        //Given
        var ruleOutcomes = List.of(buildRuleOutcome(ACCEPT_DECISION), buildRuleOutcome(ACCEPT_DECISION),
                buildRuleOutcome(ACCEPT_DECISION));
        var offers = List.of(Offer.builder().ruleOutcomes(ruleOutcomes).build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(ACCEPT_DECISION, DIP_APPLICATION_TYPE, offers);

        //Then
        assertThat(offers).hasSize(1);
        assertThat(offers.get(0).getRuleOutcomes()).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {REFER_DECISION, DECLINE_DECISION})
    void shouldNotFilterRuleOutcomesWhenRuleOutcomesIsNull(String decision) {
        //Given
        var offers = List.of(Offer.builder().build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(decision, DIP_APPLICATION_TYPE , offers);

        //Then
        assertThat(offers.get(0).getRuleOutcomes()).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {REFER_DECISION, DECLINE_DECISION})
    void shouldNotFilterRuleOutcomesWhenRuleOutcomesIsEmpty(String decision) {
        //Given
        var offers = List.of(Offer.builder().ruleOutcomes(new ArrayList<>()).build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(decision, DIP_APPLICATION_TYPE , offers);

        //Then
        assertThat(offers.get(0).getRuleOutcomes()).isNull();
    }

    @Test
    void shouldRemoveRuleOutcomesWhenApplicationTypeIsQuickQuote() {
        //Given
        List<RuleOutcome> ruleOutcomes =  List.of(buildRuleOutcome(DECLINE_DECISION));
        var offers = List.of(Offer.builder().ruleOutcomes(ruleOutcomes).build());

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(DECLINE_DECISION, "QuickQuote", offers);

        //Then
        assertThat(offers.get(0).getRuleOutcomes()).isNull();
    }

    @Test
    void shouldNotErrorIfNoOffersAndDeclineDecisionAndApplicationTypeIsQuickQuote() {
        //Given
        List<Offer> offers = new ArrayList<>();

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(DECLINE_DECISION, "QuickQuote", offers);

        //Then
        assertThat(offers).isEmpty();
    }

    @Test
    void shouldNotErrorIfOffersIsNullAndDeclineDecisionAndApplicationTypeIsQuickQuote() {
        //Given
        List<Offer> offers = null;

        //When
        ruleOutcomeFilter.filterOfferRuleOutcomes(DECLINE_DECISION, "QuickQuote", null);

        //Then
        assertThat(offers).isNull();
    }

    private RuleOutcome buildRuleOutcome(String outcome) {
        return RuleOutcome.builder().outcome(outcome).build();
    }
}