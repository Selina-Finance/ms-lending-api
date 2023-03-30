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


import static org.assertj.core.api.Assertions.assertThat;

import static com.selina.lending.internal.dto.LendingConstants.ACCEPT_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.ACCEPT_STATUS;
import static com.selina.lending.internal.dto.LendingConstants.DECLINE_DECISION;
import static com.selina.lending.internal.dto.LendingConstants.REFER_DECISION;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.Offer;

@ExtendWith(MockitoExtension.class)
class DecisionMappingServiceImplTest {

    private static final String DECISIONING_REFER = "Decisioning Refer";

    private static final String DECISIONING_DECLINE = "Decisioning Decline";
    @InjectMocks
    private DecisionMappingServiceImpl decisionMappingService;

    @Test
    void shouldMapDecisionFromReferToAccept() {
        //Given
        List<Offer> offer = List.of(Offer.builder().decision(REFER_DECISION).build(), Offer.builder().decision(REFER_DECISION).build());

        //When
        decisionMappingService.mapDecision(offer);

        //Then
        assertThat(offer.get(0).getDecision()).isEqualTo(ACCEPT_DECISION);
        assertThat(offer.get(1).getDecision()).isEqualTo(ACCEPT_DECISION);
    }

    @Test
    void shouldNotMapDecisionWhenDecisionIsAccept() {
        //Given
        List<Offer> offer = List.of(Offer.builder().decision(ACCEPT_DECISION).build());

        //When
        decisionMappingService.mapDecision(offer);

        //Then
        assertThat(offer.get(0).getDecision()).isEqualTo(ACCEPT_DECISION);
    }

    @Test
    void shouldNotMapDecisionWhenDecisionIsDecline() {
        //Given
        List<Offer> offer = List.of(Offer.builder().decision(DECLINE_DECISION).build());

        //When
        decisionMappingService.mapDecision(offer);

        //Then
        assertThat(offer.get(0).getDecision()).isEqualTo(DECLINE_DECISION);
    }

    @Test
    void shouldMapDecisionFromReferToAcceptInApplicationDecisionResponse() {
        //Given
        var appResponse = ApplicationDecisionResponse.builder().decision(REFER_DECISION).status(DECISIONING_REFER).build();

        //When
        decisionMappingService.mapDecision(appResponse);

        //Then
        assertThat(appResponse.getDecision()).isEqualTo(ACCEPT_DECISION);
        assertThat(appResponse.getStatus()).isEqualTo(ACCEPT_STATUS);
    }

    @Test
    void shouldNotMapDecisionWhenDecisionAcceptInApplicationDecisionResponse() {
        //Given
        var appResponse = ApplicationDecisionResponse.builder().decision(ACCEPT_DECISION).status(ACCEPT_STATUS).build();

        //When
        decisionMappingService.mapDecision(appResponse);

        //Then
        assertThat(appResponse.getDecision()).isEqualTo(ACCEPT_DECISION);
        assertThat(appResponse.getStatus()).isEqualTo(ACCEPT_STATUS);
    }

    @Test
    void shouldNotMapDecisionWhenDecisionDeclineInApplicationDecisionResponse() {
        //Given
        var appResponse = ApplicationDecisionResponse.builder().decision(DECLINE_DECISION).status(DECISIONING_DECLINE).build();

        //When
        decisionMappingService.mapDecision(appResponse);

        //Then
        assertThat(appResponse.getDecision()).isEqualTo(DECLINE_DECISION);
        assertThat(appResponse.getStatus()).isEqualTo(DECISIONING_DECLINE);
    }
}