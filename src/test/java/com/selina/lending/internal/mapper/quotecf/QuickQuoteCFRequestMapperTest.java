package com.selina.lending.internal.mapper.quotecf;

import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFRequest;
import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuickQuoteCFRequestMapperTest extends MapperBase {

    @Test
    void shouldMapToFilteredQuickQuoteApplicationRequest() {
        //Given
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();

        //When
        QuickQuoteCFRequest applicationRequest = QuickQuoteCFRequestMapper.INSTANCE.mapToQuickQuoteCFRequest(quickQuoteApplicationRequest);

        //Then
        assertThat(applicationRequest.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationRequest.getApplicants().size(), equalTo(1));
        assertThat(applicationRequest.getApplicants().get(0).getTitle(), equalTo(TITLE));
        assertThat(applicationRequest.getApplicants().get(0).getDateOfBirth(), equalTo(DOB));
        assertThat(applicationRequest.getApplicants().get(0).getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicationRequest.getApplicants().get(0).getLastName(), equalTo(LAST_NAME));
        assertThat(applicationRequest.getApplicants().get(0).getIncome(), notNullValue());
        assertThat(applicationRequest.getApplicants().get(0).getIncome().getIncome().size(), equalTo(1));
        assertThat(applicationRequest.getLoanInformation().getRequestedLoanAmount(), equalTo(REQUESTED_LOAN_AMOUNT.intValue()));
        assertThat(applicationRequest.getLoanInformation().getRequestedLoanTerm(), equalTo(LOAN_TERM));
        assertNull(applicationRequest.getSourceAccount());

        assertPropertyDetails(applicationRequest);

        assertLoanInformation(applicationRequest);

    }

    private void assertPropertyDetails(QuickQuoteCFRequest application) {
        var propertyDetails = application.getPropertyDetails();
        assertThat(propertyDetails, notNullValue());
        assertThat(propertyDetails.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(propertyDetails.getCity(), equalTo(CITY));
        assertThat(propertyDetails.getEstimatedValue(), equalTo(ESTIMATED_VALUE));
        assertThat(propertyDetails.getNumberOfPriorCharges(), equalTo(1));
        assertThat(propertyDetails.getPostcode(), equalTo(POSTCODE));
        assertThat(propertyDetails.getPriorCharges(), notNullValue());
    }

    private void assertLoanInformation(QuickQuoteCFRequest application) {
        var loanInformation = application.getLoanInformation();
        assertThat(loanInformation, notNullValue());
        assertThat(loanInformation.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(loanInformation.getRequestedLoanAmount(), equalTo(LOAN_AMOUNT));
        assertThat(loanInformation.getNumberOfApplicants(), equalTo(1));
        assertThat(loanInformation.getRequestedLoanTerm(), equalTo(LOAN_TERM));
        assertThat(loanInformation.getDesiredTimeLine(), equalTo(DESIRED_TIME_LINE));
        assertNull(loanInformation.getFacilities());
    }

}
