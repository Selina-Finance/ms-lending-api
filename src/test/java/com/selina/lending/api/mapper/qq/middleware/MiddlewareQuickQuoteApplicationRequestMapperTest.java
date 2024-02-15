package com.selina.lending.api.mapper.qq.middleware;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.httpclient.middleware.dto.common.Address;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import com.selina.lending.httpclient.middleware.dto.common.Expenditure;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.middleware.dto.common.Income;
import com.selina.lending.httpclient.middleware.dto.common.Incomes;
import com.selina.lending.httpclient.middleware.dto.common.LoanInformation;
import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
import com.selina.lending.httpclient.middleware.dto.qq.request.Partner;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import com.selina.lending.httpclient.adp.dto.response.Product;
import com.selina.lending.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class MiddlewareQuickQuoteApplicationRequestMapperTest extends MapperBase {

    private static final String MS_QUICK_QUOTE_CLIENT_ID = "ms-quick-quote";
    private static final String SOURCE_ACCOUNT = "Source account";
    private static final String LENDING_API_SOURCE = "LendingAPI";
    private static final String QUICK_QUOTE_FORM_SOURCE = "Quick Quote Form";
    private static final String QUICK_QUOTE_APPLICATION_TYPE = "QuickQuote";
    private static final String QQ01_PRODUCT_CODE = "QQ01";
    private static final Boolean ADD_ARRANGEMENT_FEE_SELINA_TO_LOAN = true;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MiddlewareQuickQuoteApplicationRequestMapper mapper;

    @BeforeEach
    void setUp() {
        when(tokenService.retrieveClientId()).thenReturn("the-aggregator");
    }

    @Nested
    class MergeExpendituresOfTheSameType {

        @Test
        void whenHaveTwoExpendituresOfTheSameTypeThenMergeThemIntoOne() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            var utilitiesExpenditure1 = getExpenditureDto("Utilities");
            var utilitiesExpenditure2 = getExpenditureDto("Utilities");
            var otherExpenditure = getExpenditureDto("Other");

            quickQuoteApplicationRequest.setExpenditure(List.of(utilitiesExpenditure1, utilitiesExpenditure2, otherExpenditure));

            var products = List.of(getProduct());
            var fees = Fees.builder().build();

            //When
            QuickQuoteRequest middlewareCreateApplicationEvent = mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

            //Then
            assertThat(middlewareCreateApplicationEvent.getExpenditure(), hasSize(2));
            assertThat(middlewareCreateApplicationEvent.getExpenditure(), containsInAnyOrder(
                    allOf(
                            hasProperty("expenditureType", equalTo("Utilities")),
                            hasProperty("frequency", equalTo(EXPENDITURE_FREQUENCY)),
                            hasProperty("balanceDeclared", equalTo(EXPENDITURE_BALANCE_DECLARED * 2)),
                            hasProperty("amountDeclared", equalTo(EXPENDITURE_AMOUNT_DECLARED * 2)),
                            hasProperty("paymentVerified", equalTo(EXPENDITURE_PAYMENT_VERIFIED * 2)),
                            hasProperty("amountVerified", equalTo(EXPENDITURE_AMOUNT_VERIFIED * 2))
                    ),
                    allOf(
                            hasProperty("expenditureType", equalTo("Other")),
                            hasProperty("frequency", equalTo(EXPENDITURE_FREQUENCY)),
                            hasProperty("balanceDeclared", equalTo(EXPENDITURE_BALANCE_DECLARED)),
                            hasProperty("amountDeclared", equalTo(EXPENDITURE_AMOUNT_DECLARED)),
                            hasProperty("paymentVerified", equalTo(EXPENDITURE_PAYMENT_VERIFIED)),
                            hasProperty("amountVerified", equalTo(EXPENDITURE_AMOUNT_VERIFIED))
                    )
            ));
        }

        @Test
        void whenFirstExpenditureDoesNotHaveValuesThenUseValuesOfTheSecondExpenditure() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            var utilitiesExpenditure1 = getExpenditureDto("Utilities");
            utilitiesExpenditure1.setBalanceDeclared(null);
            utilitiesExpenditure1.setAmountDeclared(null);
            utilitiesExpenditure1.setPaymentVerified(null);
            utilitiesExpenditure1.setAmountVerified(null);

            var utilitiesExpenditure2 = getExpenditureDto("Utilities");

            quickQuoteApplicationRequest.setExpenditure(List.of(utilitiesExpenditure1, utilitiesExpenditure2));

            var products = List.of(getProduct());
            var fees = Fees.builder().build();

            //When
            QuickQuoteRequest middlewareCreateApplicationEvent = mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

            //Then
            assertThat(middlewareCreateApplicationEvent.getExpenditure(), hasSize(1));
            assertThat(middlewareCreateApplicationEvent.getExpenditure(), contains(
                    allOf(
                            hasProperty("expenditureType", equalTo("Utilities")),
                            hasProperty("frequency", equalTo(EXPENDITURE_FREQUENCY)),
                            hasProperty("balanceDeclared", equalTo(EXPENDITURE_BALANCE_DECLARED)),
                            hasProperty("amountDeclared", equalTo(EXPENDITURE_AMOUNT_DECLARED)),
                            hasProperty("paymentVerified", equalTo(EXPENDITURE_PAYMENT_VERIFIED)),
                            hasProperty("amountVerified", equalTo(EXPENDITURE_AMOUNT_VERIFIED))
                    )
            ));
        }

        @Test
        void whenSecondExpenditureDoesNotHaveValuesThenUseValuesOfTheFirstExpenditure() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            var utilitiesExpenditure1 = getExpenditureDto("Utilities");
            var utilitiesExpenditure2 = getExpenditureDto("Utilities");
            utilitiesExpenditure2.setBalanceDeclared(null);
            utilitiesExpenditure2.setAmountDeclared(null);
            utilitiesExpenditure2.setPaymentVerified(null);
            utilitiesExpenditure2.setAmountVerified(null);

            quickQuoteApplicationRequest.setExpenditure(List.of(utilitiesExpenditure1, utilitiesExpenditure2));

            var products = List.of(getProduct());
            var fees = Fees.builder().build();

            //When
            QuickQuoteRequest middlewareCreateApplicationEvent = mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

            //Then
            assertThat(middlewareCreateApplicationEvent.getExpenditure(), hasSize(1));
            assertThat(middlewareCreateApplicationEvent.getExpenditure(), contains(
                    allOf(
                            hasProperty("expenditureType", equalTo("Utilities")),
                            hasProperty("frequency", equalTo(EXPENDITURE_FREQUENCY)),
                            hasProperty("balanceDeclared", equalTo(EXPENDITURE_BALANCE_DECLARED)),
                            hasProperty("amountDeclared", equalTo(EXPENDITURE_AMOUNT_DECLARED)),
                            hasProperty("paymentVerified", equalTo(EXPENDITURE_PAYMENT_VERIFIED)),
                            hasProperty("amountVerified", equalTo(EXPENDITURE_AMOUNT_VERIFIED))
                    )
            ));
        }
    }

    @Test
    void whenExpenditureTypeIsNotSpecifiedThenMapItToMonthlyValue() {
        //Given
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        var expenditure = getExpenditureDto();
        expenditure.setFrequency(null);

        quickQuoteApplicationRequest.setExpenditure(List.of(expenditure));

        var products = List.of(getProduct());
        var fees = Fees.builder().build();

        //When
        QuickQuoteRequest middlewareCreateApplicationEvent = mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

        //Then
        assertThat(middlewareCreateApplicationEvent.getExpenditure(), hasSize(1));
        assertThat(middlewareCreateApplicationEvent.getExpenditure(), contains(
                allOf(
                        hasProperty("expenditureType", equalTo("Utilities")),
                        hasProperty("frequency", equalTo("monthly"))
                )
        ));
    }

    @Test
    void shouldMapQuickQuoteApplicationRequestToMiddlewareCreateApplicationEvent() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getLoanInformation().setOriginalRequestedLoanTerm(ORIGINAL_LOAN_TERM);
        quickQuoteApplicationRequest.setTestGroupId(TEST_GROUP_ID);
        quickQuoteApplicationRequest.setPartner(getPartner());

        List<Product> products = List.of(getProduct());
        Fees fees = Fees.builder()
                .arrangementFeeDiscountSelina(ARRANGEMENT_FEE_DISCOUNT_SELINA)
                .addArrangementFeeSelina(true)
                .isAddArrangementFeeSelinaToLoan(ADD_ARRANGEMENT_FEE_SELINA_TO_LOAN)
                .build();

        //When
        QuickQuoteRequest middlewareCreateApplicationEvent = mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

        //Then
        assertThat(middlewareCreateApplicationEvent.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(middlewareCreateApplicationEvent.getSourceAccount(), equalTo(SOURCE_ACCOUNT));
        assertThat(middlewareCreateApplicationEvent.getSource(), equalTo(LENDING_API_SOURCE));
        assertThat(middlewareCreateApplicationEvent.getApplicationType(), equalTo(QUICK_QUOTE_APPLICATION_TYPE));
        assertThat(middlewareCreateApplicationEvent.getProductCode(), equalTo(QQ01_PRODUCT_CODE));
        assertThat(middlewareCreateApplicationEvent.getHasGivenConsentForMarketingCommunications(), equalTo(false));
        assertThat(middlewareCreateApplicationEvent.getIsNotContactable(), equalTo(true));
        assertThat(middlewareCreateApplicationEvent.getEligibility(), equalTo(ELIGIBILITY));
        assertThat(middlewareCreateApplicationEvent.getTestGroupId(), equalTo(TEST_GROUP_ID));

        assertApplicants(middlewareCreateApplicationEvent.getApplicants());
        assertFees(fees, middlewareCreateApplicationEvent.getFees());
        assertLead(middlewareCreateApplicationEvent.getLead());
        assertLoanInformation(middlewareCreateApplicationEvent.getLoanInformation());
        assertPropertyDetails(middlewareCreateApplicationEvent.getPropertyDetails());
        assertOffers(middlewareCreateApplicationEvent.getOffers());
        assertPartner(middlewareCreateApplicationEvent.getPartner());
        assertExpenditure(middlewareCreateApplicationEvent.getExpenditure());
    }

    @Test
    void whenHasGivenConsentForMarketingCommunicationsIsSpecifiedThenUseSpecifiedValue() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.setHasGivenConsentForMarketingCommunications(true);
        List<Product> products = List.of(getProduct());
        Fees fees = Fees.builder().build();

        //When
        QuickQuoteRequest middlewareCreateApplicationEvent = mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

        //Then
        assertThat(middlewareCreateApplicationEvent.getHasGivenConsentForMarketingCommunications(), equalTo(true));
    }

    @Test
    void whenEligibilityIsNotFoundThenMapNullToMiddlewareCreateApplicationEvent() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        List<Product> products = List.of(getProduct());
        products.get(0).getOffer().setEligibility(null);

        //When
        QuickQuoteRequest middlewareCreateApplicationEvent = mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, Fees.builder().build());

        //Then
        assertThat(middlewareCreateApplicationEvent.getEligibility(), is(nullValue()));
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

    @Test
    void whenQQApplicationCreatedByMsQuickQuoteServiceThenChangeSourceToQuickQuoteForm() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(tokenService.retrieveClientId()).thenReturn(MS_QUICK_QUOTE_CLIENT_ID);

        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        List<Product> products = List.of(getProduct());
        Fees fees = Fees.builder()
                .arrangementFeeDiscountSelina(ARRANGEMENT_FEE_DISCOUNT_SELINA)
                .addArrangementFeeSelina(true)
                .isAddArrangementFeeSelinaToLoan(ADD_ARRANGEMENT_FEE_SELINA_TO_LOAN)
                .build();

        //When
        QuickQuoteRequest middlewareCreateApplicationEvent =
                mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

        //Then
        assertThat(middlewareCreateApplicationEvent.getSource(), equalTo(QUICK_QUOTE_FORM_SOURCE));
    }

    @Test
    void whenQQApplicationCreatedByMsQuickQuoteServiceThenChangeIsNotContactableToFalse() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(tokenService.retrieveClientId()).thenReturn(MS_QUICK_QUOTE_CLIENT_ID);

        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        List<Product> products = List.of(getProduct());
        Fees fees = Fees.builder()
                .arrangementFeeDiscountSelina(ARRANGEMENT_FEE_DISCOUNT_SELINA)
                .addArrangementFeeSelina(true)
                .isAddArrangementFeeSelinaToLoan(ADD_ARRANGEMENT_FEE_SELINA_TO_LOAN)
                .build();

        //When
        QuickQuoteRequest middlewareCreateApplicationEvent =
                mapper.mapToQuickQuoteRequest(quickQuoteApplicationRequest, products, fees);

        //Then
        assertThat(middlewareCreateApplicationEvent.getIsNotContactable(), equalTo(false));
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

    private void assertFees(Fees originalFees, Fees actualFees) {
        assertThat(actualFees.getIsAddAdviceFeeToLoan(), equalTo(originalFees.getIsAddAdviceFeeToLoan()));
        assertThat(actualFees.getIsAddArrangementFeeToLoan(), equalTo(originalFees.getIsAddArrangementFeeToLoan()));
        assertThat(actualFees.getIsAddCommissionFeeToLoan(), equalTo(originalFees.getIsAddCommissionFeeToLoan()));
        assertThat(actualFees.getIsAddThirdPartyFeeToLoan(), equalTo(originalFees.getIsAddThirdPartyFeeToLoan()));
        assertThat(actualFees.getIsAddValuationFeeToLoan(), equalTo(originalFees.getIsAddValuationFeeToLoan()));
        assertThat(actualFees.getAdviceFee(), equalTo(originalFees.getAdviceFee()));
        assertThat(actualFees.getArrangementFee(), equalTo(originalFees.getArrangementFee()));
        assertThat(actualFees.getCommissionFee(), equalTo(originalFees.getCommissionFee()));
        assertThat(actualFees.getThirdPartyFee(), equalTo(originalFees.getThirdPartyFee()));
        assertThat(actualFees.getValuationFee(), equalTo(originalFees.getValuationFee()));
        assertThat(actualFees.getIsAddProductFeesToFacility(), equalTo(originalFees.getIsAddProductFeesToFacility()));
        assertThat(actualFees.getIntermediaryFeeAmount(), equalTo(originalFees.getIntermediaryFeeAmount()));
        assertThat(actualFees.getIsAddIntermediaryFeeToLoan(), equalTo(originalFees.getIsAddIntermediaryFeeToLoan()));
        assertThat(actualFees.getAddArrangementFeeSelina(), equalTo(originalFees.getAddArrangementFeeSelina()));
        assertThat(actualFees.getIsAddArrangementFeeSelinaToLoan(), equalTo(originalFees.getIsAddArrangementFeeSelinaToLoan()));
        assertThat(actualFees.getArrangementFeeDiscountSelina(), equalTo(originalFees.getArrangementFeeDiscountSelina()));
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
        assertThat(loanInformation.getOriginalRequestedLoanTerm(), equalTo(ORIGINAL_LOAN_TERM));
        assertThat(loanInformation.getNumberOfApplicants(), equalTo(1));
        assertThat(loanInformation.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(loanInformation.getDesiredTimeLine(), equalTo(DESIRED_TIME_LINE));

        var facilities = loanInformation.getFacilities();
        assertThat(facilities, hasSize(1));
        assertThat(facilities.get(0).getAllocationAmount(), equalTo(LOAN_AMOUNT.doubleValue()));
        assertThat(facilities.get(0).getAllocationPurpose(), equalTo(LOAN_PURPOSE));
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
        assertThat(offer.getNetLoanAmount(), equalTo(NET_LOAN_AMOUNT));
        assertNotNull(offer.getErcData());
    }

    private void assertPartner(Partner partner) {
        assertThat(partner.getSubUnitId(), equalTo(SUB_UNIT_ID));
        assertThat(partner.getCompanyId(), equalTo(COMPANY_ID));
    }

    private void assertExpenditure(List<Expenditure> expenditures) {
        assertThat(expenditures, hasSize(1));

        var expenditure = expenditures.get(0);
        assertThat(expenditure.getExpenditureType(), equalTo(EXPENDITURE_TYPE));
        assertThat(expenditure.getFrequency(), equalTo(EXPENDITURE_FREQUENCY));
        assertThat(expenditure.getBalanceDeclared(), equalTo(EXPENDITURE_BALANCE_DECLARED));
        assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED));
        assertThat(expenditure.getPaymentVerified(), equalTo(EXPENDITURE_PAYMENT_VERIFIED));
        assertThat(expenditure.getAmountVerified(), equalTo(EXPENDITURE_AMOUNT_VERIFIED));
    }
}
