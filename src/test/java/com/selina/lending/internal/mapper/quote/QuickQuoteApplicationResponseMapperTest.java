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

import com.selina.lending.internal.dto.quote.ProductOfferDto;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class QuickQuoteApplicationResponseMapperTest extends MapperBase {

    @Test
    void mapToQuickQuoteResponseDecisionAccepted() {
        //Given
        var filteredResponse = getFilteredQuickQuoteDecisionResponse();

        //When
        var quickQuoteResponse = QuickQuoteApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(filteredResponse);

        //Then
        assertThat(quickQuoteResponse.getStatus(), equalTo(DECISION));

        var offer = quickQuoteResponse.getOffers().get(0);

        assertThat(offer.getId(), equalTo(OFFER_ID));
        assertThat(offer.getName(), equalTo(OFFER_VARIABLE_RATE_50_LTV));
        assertThat(offer.getCategory(), equalTo(CATEGORY_STATUS_0));
        assertThat(offer.getAprc(), equalTo(APRC));
        assertThat(offer.getIsAprcHeadline(), equalTo(IS_APRC_HEADLINE));
        assertThat(offer.getEar(), equalTo(EAR));
        assertThat(offer.getSvr(), equalTo(SVR));
        assertThat(offer.getOfferBalance(), equalTo(OFFER_BALANCE));
        assertThat(offer.getHasFee(), equalTo(true));
        assertThat(offer.getInitialPayment(), equalTo(INITIAL_PAYMENT));
        assertThat(offer.getReversionPayment(), equalTo(REVERSION_PAYMENT));
        assertThat(offer.getInitialRate(), equalTo(INITIAL_RATE));
        assertThat(offer.getInitialTerm(), equalTo(LOAN_TERM));
        assertThat(offer.getReversionTerm(), equalTo(REVERSION_TERM));
        assertThat(offer.getTerm(), equalTo(LOAN_TERM));
        assertThat(offer.getMaximumLoanAmount(), equalTo(MAX_LOAN_AMOUNT));
        assertThat(offer.getProductFee(), equalTo(FEE));
        assertThat(offer.getTotalAmountRepaid(), equalTo(TOTAL_AMOUNT_REPAID));
        assertThat(offer.getHasProductFeeAddedToLoan(), equalTo(true));
        assertThat(offer.getRequestedLoanAmount(), equalTo(REQUESTED_LOAN_AMOUNT));
        assertThat(offer.getIsVariable(), equalTo(true));
        assertThat(offer.getLtvCap(), equalTo(LTV_CAP));
        assertThat(offer.getMaxErc(), equalTo(MAX_ERC));
        assertThat(offer.getEligibility(), equalTo(ELIGIBILITY));
        assertThat(offer.getArrangementFeeSelina(), equalTo(ARRANGEMENT_FEE_SELINA));
        assertThat(offer.getBrokerFeesIncluded(), equalTo(BROKER_FEES_INCLUDED));
        assertThat(offer.getBrokerFeesUpfront(), equalTo(BROKER_FEES_UPFRONT));

        assertErcData(offer);
    }

    private void assertErcData(ProductOfferDto offer) {
        var ercData = offer.getErcData();
        assertThat(offer.getHasErc(), equalTo(true));
        assertThat(offer.getErcPeriodYears(), equalTo(2));
        assertThat(offer.getErcShortCode(), equalTo(ERC_SHORT_CODE));

        assertThat(ercData.get(0).getPeriod(), equalTo(1));
        assertThat(ercData.get(0).getErcAmount(), equalTo(ERC_AMOUNT));
        assertThat(ercData.get(0).getErcBalance(), equalTo(ERC_BALANCE));
        assertThat(ercData.get(0).getErcFee(), equalTo(ERC_FEE));
        assertThat(ercData.get(1).getPeriod(), equalTo(2));
        assertThat(ercData.get(1).getErcAmount(), equalTo(ERC_AMOUNT));
        assertThat(ercData.get(1).getErcBalance(), equalTo(ERC_BALANCE));
        assertThat(ercData.get(1).getErcFee(), equalTo(ERC_FEE));
    }

    @Test
    void mapToQuickQuoteResponseDecisionDeclined() {
        //Given
        String decision = "Declined";
        var filteredResponse =
                FilteredQuickQuoteDecisionResponse.builder().decision(decision).build();

        //When
        var response = QuickQuoteApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(filteredResponse);

        //Then
        assertThat(response.getStatus(), equalTo(decision));
        assertThat(response.getOffers(), equalTo(null));
    }
}