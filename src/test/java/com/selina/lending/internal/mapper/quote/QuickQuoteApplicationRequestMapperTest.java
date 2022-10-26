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
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.application.domain.quote.Application;
import com.selina.lending.internal.service.application.domain.quote.FilteredQuickQuoteApplicationRequest;

class QuickQuoteApplicationRequestMapperTest extends MapperBase {

    @Test
    void mapToFilteredQuickQuoteApplicationRequestFromQuickQuoteApplicationRequestDto() {
        //Given
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();

        //When
        var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

        //Then
        var application = applicationRequest.getApplication();
        assertThat(application.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(application.getApplicants().size(), equalTo(1));
        assertThat(application.getApplicants().get(0).getDateOfBirth(), equalTo(LocalDate.parse(DOB)));
        assertThat(application.getApplicants().get(0).getFirstName(), equalTo(FIRST_NAME));
        assertThat(application.getApplicants().get(0).getLastName(), equalTo(LAST_NAME));
        assertThat(application.getApplicants().get(0).getIncomes(), notNullValue());
        assertThat(application.getApplicants().get(0).getIncomes().size(), equalTo(1));
        assertThat(application.getLoanInformation().getRequestedLoanAmount(), equalTo(REQUESTED_LOAN_AMOUNT));
        assertThat(application.getLoanInformation().getRequestedLoanTerm(), equalTo(LOAN_TERM));

        assertPropertyDetails(application);

        assertPriorCharges(applicationRequest);
    }

    private void assertPropertyDetails(Application application) {
        var propertyDetails = application.getPropertyDetails();
        assertThat(propertyDetails, notNullValue());
        assertThat(propertyDetails.getEstimatedValue(), equalTo(ESTIMATED_VALUE));
        assertThat(propertyDetails.getPostcode(), equalTo(POSTCODE));
    }

    private void assertPriorCharges(FilteredQuickQuoteApplicationRequest applicationRequest) {
        var priorCharges = applicationRequest.getOptions().getPriorCharges();
        assertThat(priorCharges.getNumberPriorCharges(), equalTo(1));
        assertThat(priorCharges.getMonthlyPayment(), equalTo(MONTHLY_PAYMENT));
        assertThat(priorCharges.getBalanceOutstanding(), equalTo(OUTSTANDING_BALANCE));
    }
}