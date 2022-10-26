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

package com.selina.lending.internal.mapper.quote;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.application.domain.quote.FilteredQuickQuoteDecisionResponse;

class QuickQuoteApplicationResponseMapperTest extends MapperBase {


    @Test
    void mapToQuickQuoteResponseDecisionAccept() {
        //Given
        var filteredResponse = getFilteredQuickQuoteDecisionResponse();

        //When
        var quickQuoteResponse = QuickQuoteApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(filteredResponse);

        //Then
        assertThat(quickQuoteResponse.getStatus(), equalTo(DECISION));

        var offer = quickQuoteResponse.getOffers().get(0);

        assertThat(offer.getId(), equalTo(OFFER_ID));
        assertThat(offer.getName(), equalTo(OFFER_VARIABLE_RATE_50_LTV));
        assertThat(offer.getAprc(), equalTo(APRC));
        assertThat(offer.getHasFee(), equalTo(true));
        assertThat(offer.getInitialPayment(), equalTo(INITIAL_PAYMENT));
        assertThat(offer.getInitialRate(), equalTo(INITIAL_RATE));
        assertThat(offer.getInitialTerm(), equalTo(LOAN_TERM));
        assertThat(offer.getProductFee(), equalTo(FEE));
        assertThat(offer.getTotalAmountRepaid(), equalTo(TOTAL_AMOUNT_REPAID));
        assertThat(offer.getHasProductFeeAddedToLoan(), equalTo(true));
        assertThat(offer.getRequestedLoanAmount(), equalTo(REQUESTED_LOAN_AMOUNT));
        assertThat(offer.getIsVariable(), equalTo(true));
    }

    @Test
    void mapToQuickQuoteResponseDecisionDecline() {
        //Given
        String decision = "Decline";
        var filteredResponse =
                FilteredQuickQuoteDecisionResponse.builder().decision(decision).build();

        //When
        var response = QuickQuoteApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(filteredResponse);

        //Then
        assertThat(response.getStatus(), equalTo(decision));
        assertThat(response.getOffers(), equalTo(null));
    }
}