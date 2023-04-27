package com.selina.lending.messaging.mapper.middleware;

import com.selina.lending.IntegrationTest;
import com.selina.lending.internal.dto.LeadDto;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.Address;
import com.selina.lending.internal.service.application.domain.Applicant;
import com.selina.lending.internal.service.application.domain.Erc;
import com.selina.lending.internal.service.application.domain.Fees;
import com.selina.lending.internal.service.application.domain.Incomes;
import com.selina.lending.internal.service.application.domain.LoanInformation;
import com.selina.lending.internal.service.application.domain.Offer;
import com.selina.lending.internal.service.application.domain.PropertyDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@IntegrationTest
public class MiddlewareCreateApplicationEventMapperTest extends MapperBase {

    private static final String SOURCE_ACCOUNT = "Source account";
    private static final String LENDING_API_SOURCE = "LendingAPI";
    private static final String QUICK_QUOTE_APPLICATION_TYPE = "QuickQuote";
    private static final String QQ01_PRODUCT_CODE = "QQ01";

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MiddlewareCreateApplicationEventMapper mapper;

    @Test
    void shouldMapQuickQuoteApplicationRequestToMiddlewareCreateApplicationEvent() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        var products = List.of(getProduct());

        //When
        var middlewareCreateApplicationEvent = mapper.mapToMiddlewareCreateApplicationEvent(quickQuoteApplicationRequest, products);

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
    }

    private void assertApplicants(List<Applicant> applicants) {
        assertThat(applicants, hasSize(1));

        var applicant = applicants.get(0);
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

        var address = addresses.get(0);
        assertThat(address.getAddressType(), equalTo(ADDRESS_TYPE));
        assertThat(address.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(address.getCity(), equalTo(CITY));
        assertThat(address.getPostcode(), equalTo(POSTCODE));
        assertThat(address.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(address.getBuildingNumber(), equalTo(BUILDING_NUMBER));
    }

    private void assertApplicantIncomes(Incomes incomes) {
        assertThat(incomes.getIncome(), hasSize(1));

        var income = incomes.getIncome().get(0);
        assertThat(income.getType(), equalTo(INCOME_TYPE));
        assertThat(income.getAmount(), equalTo(INCOME_AMOUNT));
    }

    private void assertFees(Fees fees) {
        var expectedFees = Fees.builder()
                .isAddProductFeesToFacility(false)
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

        var offer = offers.get(0);
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
        assertErcData(offer.getErcData());
    }

    private void assertErcData(List<Erc> ercData) {
        assertThat(ercData, hasSize(2));

        var erc = ercData.get(0);
        assertThat(erc.getPeriod(), equalTo(1));
        assertThat(erc.getErcFee(), equalTo(ERC_FEE));
        assertThat(erc.getErcBalance(), equalTo(ERC_BALANCE));
        assertThat(erc.getErcAmount(), equalTo(ERC_AMOUNT));
    }
}
