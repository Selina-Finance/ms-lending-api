package com.selina.lending.messaging.mapper;

import com.selina.lending.internal.dto.LeadDto;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.application.domain.Address;
import com.selina.lending.internal.service.application.domain.Applicant;
import com.selina.lending.internal.service.application.domain.Incomes;
import com.selina.lending.internal.service.application.domain.LoanInformation;
import com.selina.lending.internal.service.application.domain.PropertyDetails;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MiddlewareCreateApplicationEventMapperTest extends MapperBase {

    @Test
    void shouldMapQuickQuoteApplicationRequestToMiddlewareCreateApplicationEvent() {
        //Given
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        
        //When
        var middlewareCreateApplicationEvent = MiddlewareCreateApplicationEventMapper.INSTANCE
                .mapToMiddlewareCreateApplicationEvent(quickQuoteApplicationRequest);
        
        //Then
        assertThat(middlewareCreateApplicationEvent.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));

        // TODO
        //check sourceAccount
        //check source
        //check applicationType
        //check productCode

        assertThat(middlewareCreateApplicationEvent.getLoanInformation().getRequestedLoanAmount(), equalTo(REQUESTED_LOAN_AMOUNT.intValue()));
        assertThat(middlewareCreateApplicationEvent.getLoanInformation().getRequestedLoanTerm(), equalTo(LOAN_TERM));
        assertNull(middlewareCreateApplicationEvent.getSourceAccount());
        assertApplicants(middlewareCreateApplicationEvent.getApplicants());
        assertPropertyDetails(middlewareCreateApplicationEvent.getPropertyDetails());
        assertLoanInformation(middlewareCreateApplicationEvent.getLoanInformation());

        // TODO do we need to send hasGivenConsentForMarketingCommunications ?

        assertLead(middlewareCreateApplicationEvent.getLead());
    }
    
    private void assertApplicants(List<Applicant> applicants) {
        assertThat(applicants.size(), equalTo(1));

        var firstApplicant = applicants.get(0);
        assertThat(firstApplicant.getTitle(), equalTo(TITLE));
        assertThat(firstApplicant.getEmailAddress(), equalTo(EMAIL_ADDRESS));
        assertThat(firstApplicant.getMobilePhoneNumber(), equalTo(MOBILE_NUMBER));
        assertThat(firstApplicant.getFirstName(), equalTo(FIRST_NAME));
        assertThat(firstApplicant.getLastName(), equalTo(LAST_NAME));
        assertThat(firstApplicant.getDateOfBirth(), equalTo(DOB));

        assertApplicantAddresses(firstApplicant.getAddresses());
        assertApplicantIncomes(firstApplicant.getIncome());
    }

    private void assertApplicantAddresses(List<Address> addresses) {
        assertThat(addresses.size(), equalTo(1));

        var firstAddress = addresses.get(0);
        assertThat(firstAddress.getAddressType(), equalTo(ADDRESS_TYPE));
        assertThat(firstAddress.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(firstAddress.getCity(), equalTo(CITY));
        assertThat(firstAddress.getPostcode(), equalTo(POSTCODE));
        assertThat(firstAddress.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(firstAddress.getBuildingNumber(), equalTo(BUILDING_NUMBER));
        assertThat(firstAddress.getBuildingNumber(), equalTo(BUILDING_NUMBER));
    }

    private void assertApplicantIncomes(Incomes income) {
        assertThat(income, notNullValue());
        assertThat(income.getIncome().size(), equalTo(1));

        var firstIncome = income.getIncome().get(0);
        assertThat(firstIncome.getType(), equalTo(INCOME_TYPE));
        assertThat(firstIncome.getAmount(), equalTo(INCOME_AMOUNT));
    }

    private void assertPropertyDetails(PropertyDetails propertyDetails) {
        assertThat(propertyDetails, notNullValue());
        assertThat(propertyDetails.getEstimatedValue(), equalTo(ESTIMATED_VALUE));
        assertThat(propertyDetails.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(propertyDetails.getCity(), equalTo(CITY));
        assertThat(propertyDetails.getPostcode(), equalTo(POSTCODE));
        assertThat(propertyDetails.getBuildingName(), equalTo(BUILDING_NUMBER));
        assertThat(propertyDetails.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(propertyDetails.getNumberOfPriorCharges(), equalTo(1));
        assertThat(propertyDetails.getPriorCharges(), notNullValue());
    }

    private void assertLoanInformation(LoanInformation loanInformation) {
        assertThat(loanInformation, notNullValue());
        assertThat(loanInformation.getRequestedLoanAmount(), equalTo(LOAN_AMOUNT));
        assertThat(loanInformation.getRequestedLoanTerm(), equalTo(LOAN_TERM));
        assertThat(loanInformation.getNumberOfApplicants(), equalTo(1));
        assertThat(loanInformation.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(loanInformation.getDesiredTimeLine(), equalTo(DESIRED_TIME_LINE));
        assertNull(loanInformation.getFacilities()); // TODO https://www.notion.so/selinaops/Kafka-Model-08f4585a453f453bb7649828b83260a9?pvs=4#57e3b344c2d64ba495e3401c79276015
    }

    private void assertLead(LeadDto lead) {
        assertThat(lead.getUtmSource(), equalTo(UTM_SOURCE));
        assertThat(lead.getUtmCampaign(), equalTo(UTM_CAMPAIGN));
        assertThat(lead.getUtmMedium(), equalTo(UTM_MEDIUM));
    }
}
