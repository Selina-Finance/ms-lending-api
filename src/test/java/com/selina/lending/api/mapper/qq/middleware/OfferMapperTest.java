package com.selina.lending.api.mapper.qq.middleware;

import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.httpclient.middleware.dto.common.Erc;
import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.adp.dto.response.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OfferMapperTest extends MapperBase {

    @Autowired
    OfferMapper offerMapper;

    @Test
    void shouldMapProductToOffer() {
        //Given
        Product product = getProduct();

        //When
        Offer offer = offerMapper.mapToOffer(product);

        //Then
        assertNotNull(offer);
        assertThat(offer.getId(), equalTo(OFFER_ID));
        assertThat(offer.getProductFeeCanAdd(), equalTo(true));
        assertThat(offer.getAprc(), equalTo(APRC));
        assertThat(offer.getCltv(), equalTo(CLTV));
        assertThat(offer.getOfferBalance(), equalTo(OFFER_BALANCE));
        assertThat(offer.getInitialTerm(), equalTo(LOAN_TERM));
        assertThat(offer.getInitialRate(), equalTo(INITIAL_RATE));
        assertThat(offer.getBrokerFeesIncluded(), equalTo(BROKER_FEES_INCLUDED));
        assertThat(offer.getReversionTerm(), equalTo(REVERSION_TERM.doubleValue()));
        assertThat(offer.getSvr(), equalTo(SVR));
        assertThat(offer.getProcFee(), equalTo(PROC_FEE));
        assertThat(offer.getLti(), equalTo(LTI));
        assertThat(offer.getLtvCap(), equalTo(LTV_CAP));
        assertThat(offer.getTotalAmountRepaid(), equalTo(TOTAL_AMOUNT_REPAID));
        assertThat(offer.getInitialPayment(), equalTo(INITIAL_PAYMENT));
        assertThat(offer.getReversionPayment(), equalTo(REVERSION_PAYMENT));
        assertThat(offer.getAffordabilityDeficit(), equalTo(AFFORDABILITY_DEFICIT));
        assertThat(offer.getProductCode(), equalTo(CODE));
        assertThat(offer.getProduct(), equalTo(OFFER_VARIABLE_RATE_50_LTV));
        assertThat(offer.getDecision(), equalTo(OFFER_DECISION_ACCEPT));
        assertThat(offer.getProductFee(), equalTo(FEE));
        assertThat(offer.getProductFeeAddedToLoan(), equalTo(true));
        assertThat(offer.getBrokerFeesUpfront(), equalTo(BROKER_FEES_UPFRONT));
        assertThat(offer.getHasFee(), equalTo(true));
        assertThat(offer.getIsVariable(), equalTo(true));
        assertThat(offer.getCategory(), equalTo(CATEGORY_STATUS_0));
        assertThat(offer.getFamily(), equalTo(HOMEOWNER_LOAN));
        assertThat(offer.getVariant(), equalTo(OFFER_VARIANT));
        assertThat(offer.getMaximumBalanceEsis(), equalTo(MAX_BALANCE_ESIS));
        assertThat(offer.getMaxErc(), equalTo(MAX_ERC));
        assertThat(offer.getErcProfile(), equalTo(ERC_PROFILE));
        assertThat(offer.getErcShortcode(), equalTo(ERC_SHORT_CODE));
        assertThat(offer.getErcPeriodYears(), equalTo(ERC_PERIOD_YEARS));
        assertThat(offer.getTerm(), equalTo(LOAN_TERM));
        assertThat(offer.getEar(), equalTo(EAR));
        assertThat(offer.getMaximumLoanAmount(), equalTo(MAX_LOAN_AMOUNT));
        assertThat(offer.getRequestedLoanAmount(), equalTo(REQUESTED_LOAN_AMOUNT));
        assertThat(offer.getHasErc(), equalTo(true));
        assertThat(offer.getExpiryDate(), equalTo(EXPIRY_DATE));
        assertThat(offer.getDtir(), equalTo(DTIR));
        assertThat(offer.getBbr(), equalTo(BBR));
        assertThat(offer.getInitialMargin(), equalTo(INITIAL_MARGIN));
        assertThat(offer.getPoundPaidPerBorrowed(), equalTo(POUND_PAID_PER_BORROWED));
        assertThat(offer.getMonthlyPaymentStressed(), equalTo(MONTHLY_PAYMENT_STRESSED));
        assertThat(offer.getDecisionAml(), equalTo(DECISION_AML));
        assertThat(offer.getDecisionFraud(), equalTo(DECISION_FRAUD));
        assertThat(offer.getBaseRateStressed(), equalTo(BASE_RATE_STRESSED));
        assertThat(offer.getAprcStressed(), equalTo(APRC_STRESSED));
        assertThat(offer.getInitialRateMinimum(), equalTo(INITIAL_RATE_MINIMUM));
        assertThat(offer.getReversionMargin(), equalTo(REVERSION_MARGIN));
        assertThat(offer.getReversionRateMinimum(), equalTo(REVERSION_RATE_MINIMUM));
        assertThat(offer.getDrawdownTerm(), equalTo(DRAWDOWN_TERM));
        assertThat(offer.getEsisLtvCappedBalance(), equalTo(ESIS_LTV_CAPPED_BALANCE));
        assertThat(offer.getIncomePrimaryApplicant(), equalTo(INCOME_PRIMARY_APPLICANT));
        assertThat(offer.getIncomeJointApplicant(), equalTo(INCOME_JOINT_APPLICANT));
        assertThat(offer.getDaysUntilInitialDrawdown(), equalTo(DAYS_UNTIL_INITIAL_DRAWDOWN));
        assertThat(offer.getFixedTermYears(), equalTo(FIXED_TERM_YEARS));
        assertThat(offer.getMinimumInitialDrawdown(), equalTo(MINIMUM_INITIAL_DRAWDOWN));
        assertThat(offer.getOfferValidity(), equalTo(OFFER_VALIDITY));
        assertThat(offer.getEsisLoanAmount(), equalTo(ESIS_LOAN_AMOUNT));
        assertThat(offer.getArrangementFeeSelina(), equalTo(ARRANGEMENT_FEE_SELINA));
        assertErcData(offer.getErcData());
    }

    private void assertErcData(List<Erc> ercData) {
        assertThat(ercData, hasSize(2));

        Erc erc = ercData.get(0);
        assertThat(erc.getPeriod(), equalTo(1));
        assertThat(erc.getErcFee(), equalTo(ERC_FEE));
        assertThat(erc.getErcBalance(), equalTo(ERC_BALANCE));
        assertThat(erc.getErcAmount(), equalTo(ERC_AMOUNT));
    }
}
