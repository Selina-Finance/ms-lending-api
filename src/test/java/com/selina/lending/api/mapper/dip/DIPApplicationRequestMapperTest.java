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

package com.selina.lending.api.mapper.dip;

import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import com.selina.lending.api.mapper.MapperBase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class DIPApplicationRequestMapperTest extends MapperBase {

    @Test
    void mapToApplicationRequestFromApplicationRequestDto() {
        //Given
        var applicationRequestDto = getApplicationRequestDto();

        //When
        var applicationRequest = DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(applicationRequestDto);

        //Then
        assertThat(applicationRequest.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationRequest.getExpenditure(), notNullValue());
        assertThat(applicationRequest.getExpenditure().size(), equalTo(1));
        assertThat(applicationRequest.getExpenditure().get(0).getExpenditureType(), equalTo(EXPENDITURE_TYPE));
    }

    @Test
    void mapToApplicationRequestFromDIPApplicationRequestDto() {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();

        //When
        var applicationRequest = DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);

        //Then
        assertThat(applicationRequest.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationRequest.getBrokerSubmitterEmail(), equalTo(BROKER_SUBMITTER_EMAIL));
        assertThat(applicationRequest.getExpenditure(), notNullValue());
        assertThat(applicationRequest.getExpenditure().size(), equalTo(1));
        assertThat(applicationRequest.getExpenditure().get(0).getExpenditureType(), equalTo(EXPENDITURE_TYPE));
        assertThat(applicationRequest.getApplicants().size(), equalTo(1));
        assertThat(applicationRequest.getApplicants().get(0).getIncome(), notNullValue());
        assertThat(applicationRequest.getApplicants().get(0).getIncome().getIncome().size(), equalTo(1));
        assertThat(applicationRequest.getApplicants().get(0).getAddresses().size(), equalTo(1));
        assertThat(applicationRequest.getApplicants().get(0).getEmployment(), notNullValue());
        assertThat(applicationRequest.getApplicants().get(0).getEmployment().getInProbationPeriod(), equalTo(false));
        assertThat(applicationRequest.getLoanInformation().getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(applicationRequest.getLoanInformation().getFacilities().size(), equalTo(1));
        assertThat(applicationRequest.getPropertyDetails(), notNullValue());
        assertThat(applicationRequest.getPropertyDetails().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(applicationRequest.getPropertyDetails().getAddressLine2(), equalTo(ADDRESS_LINE_2));

        assertPriorCharges(applicationRequest);

        assertFees(applicationRequest);
    }

    private void assertFees(ApplicationRequest applicationRequest) {
        var fees = applicationRequest.getFees();
        assertThat(fees, notNullValue());
        assertThat(fees.getIsAddAdviceFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddArrangementFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddCommissionFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddValuationFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddThirdPartyFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddIntermediaryFeeToLoan(), equalTo(false));
        assertThat(fees.getArrangementFee(), equalTo(ARRANGEMENT_FEE));
    }

    private void assertPriorCharges(ApplicationRequest applicationRequest) {
        var priorCharges = applicationRequest.getPropertyDetails().getPriorCharges();
        assertThat(priorCharges.getBalanceOutstanding(), equalTo(OUTSTANDING_BALANCE));
        assertThat(priorCharges.getMonthlyPayment(), equalTo(MONTHLY_PAYMENT));
        assertThat(priorCharges.getBalanceConsolidated(), equalTo(BALANCE_CONSOLIDATED));
        assertThat(priorCharges.getOtherDebtPayments(), equalTo(OTHER_DEBT_PAYMENTS));
    }
}