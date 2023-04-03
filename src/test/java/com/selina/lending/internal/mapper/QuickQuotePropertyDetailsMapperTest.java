package com.selina.lending.internal.mapper;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class QuickQuotePropertyDetailsMapperTest extends MapperBase{


    @Test
    void mapToPropertyDetailsFromPropertyDetailsDto() {
        //Given
        var quickQuotePropertyDetailsDto = getQuickQuotePropertyDetailsDto();

        //When
        var propertyDetails = QuickQuotePropertyDetailsMapper.INSTANCE.mapToPropertyDetails(quickQuotePropertyDetailsDto);

        //Then
        assertThat(propertyDetails.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(propertyDetails.getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(propertyDetails.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(propertyDetails.getBuildingNumber(), equalTo(BUILDING_NUMBER));
        assertThat(propertyDetails.getCity(), equalTo(CITY));
        assertThat(propertyDetails.getCountry(), equalTo(COUNTRY));
        assertThat(propertyDetails.getPostcode(), equalTo(POSTCODE));
        assertThat(propertyDetails.getCounty(), equalTo(COUNTY));
        assertThat(propertyDetails.getPriorCharges().getMonthlyPayment(), equalTo(MONTHLY_PAYMENT));
        assertThat(propertyDetails.getPriorCharges().getBalanceOutstanding(), equalTo(OUTSTANDING_BALANCE));
        assertThat(propertyDetails.getPriorCharges().getOtherDebtPayments(), equalTo(OTHER_DEBT_PAYMENTS));
        assertThat(propertyDetails.getPriorCharges().getBalanceConsolidated(), equalTo(BALANCE_CONSOLIDATED));

    }
}
