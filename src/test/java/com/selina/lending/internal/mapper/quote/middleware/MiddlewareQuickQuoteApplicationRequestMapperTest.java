package com.selina.lending.internal.mapper.quote.middleware;



import com.selina.lending.httpclient.middleware.dto.common.Address;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.middleware.dto.common.Income;
import com.selina.lending.httpclient.middleware.dto.common.Incomes;
import com.selina.lending.httpclient.middleware.dto.common.LoanInformation;
import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
import com.selina.lending.httpclient.middleware.dto.qq.request.Partner;
import com.selina.lending.internal.dto.LeadDto;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.httpclient.selection.dto.response.Product;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class MiddlewareQuickQuoteApplicationRequestMapperTest extends MapperBase {

    private static final String SOURCE_ACCOUNT = "Source account";
    private static final String LENDING_API_SOURCE = "LendingAPI";
    private static final String QUICK_QUOTE_APPLICATION_TYPE = "QuickQuote";
    private static final String QQ01_PRODUCT_CODE = "QQ01";

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MiddlewareQuickQuoteApplicationRequestMapper mapper;

    @Test
    void shouldMapQuickQuoteApplicationRequestToMiddlewareCreateApplicationEvent() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        List<Product> products = List.of(getProduct());
        Fees fees = Fees.builder().arrangementFeeDiscountSelina(ARRANGEMENT_FEE_DISCOUNT_SELINA).addArrangementFeeSelina(true).build();

        //When
        QuickQuoteRequest middlewareCreateApplicationEvent =
                mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

        //Then
        assertThat(middlewareCreateApplicationEvent.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(middlewareCreateApplicationEvent.getSourceAccount(), equalTo(SOURCE_ACCOUNT));
        assertThat(middlewareCreateApplicationEvent.getSource(), equalTo(LENDING_API_SOURCE));
        assertThat(middlewareCreateApplicationEvent.getApplicationType(), equalTo(QUICK_QUOTE_APPLICATION_TYPE));
        assertThat(middlewareCreateApplicationEvent.getProductCode(), equalTo(QQ01_PRODUCT_CODE));
        assertThat(middlewareCreateApplicationEvent.getHasGivenConsentForMarketingCommunications(), equalTo(false));

        assertApplicants(middlewareCreateApplicationEvent.getApplicants());
        assertFees(middlewareCreateApplicationEvent.getFees());
        assertLead(middlewareCreateApplicationEvent.getLead());
        assertLoanInformation(middlewareCreateApplicationEvent.getLoanInformation());
        assertPropertyDetails(middlewareCreateApplicationEvent.getPropertyDetails());
        assertOffers(middlewareCreateApplicationEvent.getOffers());
        assertPartner(middlewareCreateApplicationEvent.getPartner());
    }

    @Test
    void shouldMapQuickQuoteApplicationRequestWithFeesToMiddlewareCreateApplicationEvent() {
        // Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();
        List<Product> products = List.of(getProduct());
        Fees fees = Fees.builder()
                .arrangementFeeDiscountSelina(ARRANGEMENT_FEE_DISCOUNT_SELINA)
                .addArrangementFeeSelina(true)
                .isAddAdviceFeeToLoan(true)
                .isAddArrangementFeeToLoan(true)
                .isAddCommissionFeeToLoan(true)
                .isAddThirdPartyFeeToLoan(true)
                .isAddValuationFeeToLoan(true)
                .adviceFee(FEE)
                .arrangementFee(ARRANGEMENT_FEE)
                .commissionFee(FEE)
                .thirdPartyFee(FEE)
                .valuationFee(FEE)
                .isAddProductFeesToFacility(false)
                .intermediaryFeeAmount(FEE)
                .isAddIntermediaryFeeToLoan(false)
                .build();

        //When
        QuickQuoteRequest middlewareCreateApplicationEvent =
                mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

        //Then

        assertFeesWithAllOtherFees(middlewareCreateApplicationEvent.getFees());
    }

    private void assertApplicants(List<Applicant> applicants) {
        assertThat(applicants, hasSize(1));

        Applicant applicant = applicants.get(0);
        assertThat(applicant.getTitle(), equalTo(TITLE));
        assertThat(applicant.getEmailAddress(), equalTo(EMAIL_ADDRESS));
        assertThat(applicant.getMobilePhoneNumber(), equalTo(MOBILE_NUMBER));
        assertThat(applicant.getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicant.getLastName(), equalTo(LAST_NAME));
        assertThat(applicant.getDateOfBirth(), equalTo(DOB));

        assertApplicantAddresses(applicant.getAddresses());
        assertApplicantIncomes(applicant.getIncome());
    }

    private void assertApplicantAddresses(List<Address> addresses) {
        assertThat(addresses, hasSize(1));

        Address address = addresses.get(0);
        assertThat(address.getAddressType(), equalTo(ADDRESS_TYPE));
        assertThat(address.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(address.getCity(), equalTo(CITY));
        assertThat(address.getPostcode(), equalTo(POSTCODE));
        assertThat(address.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(address.getBuildingNumber(), equalTo(BUILDING_NUMBER));
    }

    private void assertApplicantIncomes(Incomes incomes) {
        assertThat(incomes.getIncome(), hasSize(1));

        Income income = incomes.getIncome().get(0);
        assertThat(income.getType(), equalTo(INCOME_TYPE));
        assertThat(income.getAmount(), equalTo(INCOME_AMOUNT));
    }

    private void assertFees(Fees fees) {
        Fees expectedFees = Fees.builder()
                .isAddProductFeesToFacility(false)
                .addArrangementFeeSelina(true)
                .arrangementFeeDiscountSelina(ARRANGEMENT_FEE_DISCOUNT_SELINA)
                .build();

        assertThat(fees, equalTo(expectedFees));
    }

    private void assertFeesWithAllOtherFees(Fees fees) {
        Fees expectedFees = Fees.builder()
                .arrangementFeeDiscountSelina(ARRANGEMENT_FEE_DISCOUNT_SELINA)
                .addArrangementFeeSelina(true)
                .isAddAdviceFeeToLoan(true)
                .isAddArrangementFeeToLoan(true)
                .isAddCommissionFeeToLoan(true)
                .isAddThirdPartyFeeToLoan(true)
                .isAddValuationFeeToLoan(true)
                .adviceFee(FEE)
                .arrangementFee(ARRANGEMENT_FEE)
                .commissionFee(FEE)
                .thirdPartyFee(FEE)
                .valuationFee(FEE)
                .isAddProductFeesToFacility(false)
                .intermediaryFeeAmount(FEE)
                .isAddIntermediaryFeeToLoan(false)
                .build();

        assertThat(fees, equalTo(expectedFees));
    }

    private void assertLead(LeadDto lead) {
        assertThat(lead.getUtmSource(), equalTo(UTM_SOURCE));
        assertThat(lead.getUtmCampaign(), equalTo(UTM_CAMPAIGN));
        assertThat(lead.getUtmMedium(), equalTo(UTM_MEDIUM));
    }

    private void assertLoanInformation(LoanInformation loanInformation) {
        assertThat(loanInformation.getRequestedLoanAmount(), equalTo(LOAN_AMOUNT));
        assertThat(loanInformation.getRequestedLoanTerm(), equalTo(LOAN_TERM));
        assertThat(loanInformation.getNumberOfApplicants(), equalTo(1));
        assertThat(loanInformation.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(loanInformation.getDesiredTimeLine(), equalTo(DESIRED_TIME_LINE));
    }

    private void assertPropertyDetails(PropertyDetails propertyDetails) {
        assertThat(propertyDetails.getEstimatedValue(), equalTo(ESTIMATED_VALUE));
        assertThat(propertyDetails.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(propertyDetails.getCity(), equalTo(CITY));
        assertThat(propertyDetails.getPostcode(), equalTo(POSTCODE));
        assertThat(propertyDetails.getBuildingNumber(), equalTo(BUILDING_NUMBER));
        assertThat(propertyDetails.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(propertyDetails.getNumberOfPriorCharges(), equalTo(1));
        assertThat(propertyDetails.getPriorCharges(), notNullValue());
    }

    private void assertOffers(List<Offer> offers) {
        assertThat(offers, hasSize(1));

        Offer offer = offers.get(0);
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
        assertNotNull(offer.getErcData());
    }

    private void assertPartner(Partner partner) {
        assertThat(partner.getSubUnitId(), equalTo(SUB_UNIT_ID));
        assertThat(partner.getCompanyId(), equalTo(COMPANY_ID));
    }

}
