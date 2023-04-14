package com.selina.lending.internal.mapper.quotecf;

import com.selina.lending.internal.dto.quote.ProductOfferDto;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFResponse;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuickQuoteCFResponseMapperTest extends MapperBase {

    @Test
    void mapToQuickQuoteResponseDecisionAccepted() {
        //Given
        QuickQuoteCFResponse response = getQuickQuoteCFResponse();

        //When
        var quickQuoteResponse = QuickQuoteCFResponseMapper.INSTANCE.mapToQuickQuoteResponse(response);

        //Then
        assertThat(quickQuoteResponse.getStatus(), equalTo(DECISION));
        assertThat(quickQuoteResponse.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));

        assertThat(quickQuoteResponse.getOffers(), notNullValue());
        assertThat(quickQuoteResponse.getOffers().size(), equalTo(1));

        var productOfferDto = quickQuoteResponse.getOffers().get(0);

        assertThat(productOfferDto.getId(), equalTo(OFFER_ID));
        assertTrue(productOfferDto.getHasFee());
        assertThat(productOfferDto.getCode(), equalTo(PRODUCT_CODE));
        assertThat(productOfferDto.getName(), equalTo(PRODUCT_NAME));
        assertThat(productOfferDto.getFamily(), equalTo(HOMEOWNER_LOAN));
        assertThat(productOfferDto.getCategory(), equalTo(CATEGORY_STATUS_0));
        assertThat(productOfferDto.getErcPeriodYears(), equalTo(2));
        assertThat(productOfferDto.getErcShortCode(), equalTo(ERC_SHORT_CODE));
        assertThat(productOfferDto.getMaxErc(), equalTo(MAX_ERC));
        assertThat(productOfferDto.getDecision(), equalTo(OFFER_DECISION_ACCEPT));
        assertThat(productOfferDto.getInitialRate(), equalTo(INITIAL_RATE));
        assertThat(productOfferDto.getInitialTerm(), equalTo(INITIAL_TERM));
        assertThat(productOfferDto.getInitialPayment(), equalTo(INITIAL_PAYMENT));
        assertThat(productOfferDto.getReversionTerm(), equalTo(REVERSION_TERM));
        assertTrue(productOfferDto.getIsVariable());
        assertThat(productOfferDto.getLtvCap(), equalTo(LTV_CAP));
        assertThat(productOfferDto.getSvr(), equalTo(SVR));
        assertThat(productOfferDto.getProductFee(), equalTo(PRODUCT_FEE));
        assertThat(productOfferDto.getTotalAmountRepaid(), equalTo(TOTAL_AMOUNT_REPAID));
        assertThat(productOfferDto.getOfferBalance(), equalTo(OFFER_BALANCE));
        assertTrue(productOfferDto.getHasProductFeeAddedToLoan());
        assertThat(productOfferDto.getRequestedLoanAmount(), equalTo(REQUESTED_LOAN_AMOUNT));
        assertThat(productOfferDto.getMaximumLoanAmount(), equalTo(MAX_LOAN_AMOUNT));
        assertThat(productOfferDto.getTerm(), equalTo(LOAN_TERM));
        assertThat(productOfferDto.getEar(), equalTo(EAR));
        assertTrue(productOfferDto.getHasErc());


        assertErcData(productOfferDto);

    }

    private void assertErcData(ProductOfferDto productOfferDto) {
        var ercData = productOfferDto.getErcData();
        assertThat(ercData, notNullValue());
        assertThat(ercData.size(), equalTo(2));
        assertThat(ercData.get(0).getErcFee(), equalTo(ERC_FEE));
        assertThat(ercData.get(0).getErcBalance(), equalTo(ERC_BALANCE));
        assertThat(ercData.get(0).getErcAmount(), equalTo(ERC_AMOUNT));
        assertThat(ercData.get(0).getPeriod(), equalTo(1));
    }


}
