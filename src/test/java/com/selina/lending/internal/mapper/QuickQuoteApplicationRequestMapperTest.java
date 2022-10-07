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

package com.selina.lending.internal.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.selina.lending.internal.service.application.domain.ApplicationRequest;

class QuickQuoteApplicationRequestMapperTest extends MapperBase{


    @Test
    void mapToApplicationRequestFromQuickQuoteApplicationRequestDto() {
        //Given
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();

        //When
        var applicationRequest = QuickQuoteApplicationRequestMapper.INSTANCE.mapToApplicationRequest(quickQuoteApplicationRequest);

        //Then
        assertThat(applicationRequest.getApplicationType(), equalTo(APPLICATION_TYPE));
        assertThat(applicationRequest.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationRequest.getExpenditure(), notNullValue());
        assertThat(applicationRequest.getExpenditure().size(), equalTo(1));
        assertThat(applicationRequest.getExpenditure().get(0).getExpenditureType(), equalTo(EXPENDITURE_TYPE));
        assertThat(applicationRequest.getApplicants().size(), equalTo(1));
        assertThat(applicationRequest.getApplicants().get(0).getIncome(), notNullValue());
        assertThat(applicationRequest.getApplicants().get(0).getIncome().getIncome().size(), equalTo(1));
        assertThat(applicationRequest.getApplicants().get(0).getAddresses().size(), equalTo(1));
        assertThat(applicationRequest.getLoanInformation().getLoanPurpose(), equalTo(LOAN_PURPOSE));

        assertPropertyDetails(applicationRequest);

        assertPriorCharges(applicationRequest);
    }

    private void assertPropertyDetails(ApplicationRequest applicationRequest) {
        var propertyDetails = applicationRequest.getPropertyDetails();
        assertThat(propertyDetails, notNullValue());
        assertThat(propertyDetails.getPurchaseValue(), equalTo(PURCHASE_VALUE));
        assertThat(propertyDetails.getEstimatedValue(), equalTo(ESTIMATED_VALUE));
        assertThat(propertyDetails.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(propertyDetails.getAddressLine2(), equalTo(ADDRESS_LINE_2));
    }

    private void assertPriorCharges(ApplicationRequest applicationRequest) {
        var priorCharges = applicationRequest.getPropertyDetails().getPriorCharges().get(0);
        assertThat(priorCharges.getName(), equalTo(HSBC));
        assertThat(priorCharges.getRateType(), equalTo(RATE_TYPE));
        assertThat(priorCharges.getRepaymentType(), equalTo(REPAYMENT_TYPE));
        assertThat(priorCharges.getMonthlyPayment(), equalTo(MONTHLY_PAYMENT));
    }
}