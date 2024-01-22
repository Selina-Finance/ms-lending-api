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

package com.selina.lending.api.mapper.dipcc;

import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.httpclient.middleware.dto.common.Expenditure;
import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

class DIPCCApplicationRequestMapperTest extends MapperBase {


    @Test
    void mapToApplicationRequestFromDIPCCApplicationRequestDto() {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();

        //When
        var applicationRequest = DIPCCApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);

        //Then
        assertThat(applicationRequest.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationRequest.getBrokerSubmitterEmail(), equalTo(BROKER_SUBMITTER_EMAIL));
        assertThat(applicationRequest.getSourceClientId(), equalTo(SOURCE_CLIENT_ID));
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

        assertFees(applicationRequest);
        assertExpenditures(applicationRequest.getExpenditure());
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

    private void assertExpenditures(List<Expenditure> expenditures) {
        assertThat(expenditures, hasSize(1));

        var expenditure = expenditures.get(0);
        assertThat(expenditure.getExpenditureType(), equalTo(EXPENDITURE_TYPE));
        assertThat(expenditure.getFrequency(), equalTo(EXPENDITURE_FREQUENCY));
        assertThat(expenditure.getBalanceDeclared(), equalTo(EXPENDITURE_BALANCE_DECLARED));
        assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED));
        assertThat(expenditure.getPaymentVerified(), equalTo(EXPENDITURE_PAYMENT_VERIFIED));
        assertThat(expenditure.getAmountVerified(), equalTo(EXPENDITURE_AMOUNT_VERIFIED));
    }

    @Test
    void whenExpenditureTypeIsNotSpecifiedThenMapItToMonthlyValue() {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var expenditure = getExpenditureDto();
        expenditure.setFrequency(null);

        dipApplicationRequest.setExpenditure(List.of(expenditure));

        //When
        var applicationRequest = DIPCCApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);

        //Then
        assertThat(applicationRequest.getExpenditure(), hasSize(1));
        assertThat(applicationRequest.getExpenditure(), contains(
                allOf(
                        hasProperty("expenditureType", equalTo("Utilities")),
                        hasProperty("frequency", equalTo("monthly"))
                )
        ));
    }

    @Nested
    class MergeExpendituresOfTheSameType {

        @Test
        void whenHaveTwoExpendituresOfTheSameTypeThenMergeThemIntoOne() {
            //Given
            var dipApplicationRequest = getDIPCCApplicationRequestDto();
            var utilitiesExpenditure1 = getExpenditureDto("Utilities");
            var utilitiesExpenditure2 = getExpenditureDto("Utilities");
            var otherExpenditure = getExpenditureDto("Other");

            dipApplicationRequest.setExpenditure(List.of(utilitiesExpenditure1, utilitiesExpenditure2, otherExpenditure));

            //When
            var applicationRequest = DIPCCApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);

            //Then
            assertThat(applicationRequest.getExpenditure(), hasSize(2));
            assertThat(applicationRequest.getExpenditure(), containsInAnyOrder(
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
            var dipApplicationRequest = getDIPCCApplicationRequestDto();
            var utilitiesExpenditure1 = getExpenditureDto("Utilities");
            utilitiesExpenditure1.setBalanceDeclared(null);
            utilitiesExpenditure1.setAmountDeclared(null);
            utilitiesExpenditure1.setPaymentVerified(null);
            utilitiesExpenditure1.setAmountVerified(null);

            var utilitiesExpenditure2 = getExpenditureDto("Utilities");

            dipApplicationRequest.setExpenditure(List.of(utilitiesExpenditure1, utilitiesExpenditure2));

            //When
            var applicationRequest = DIPCCApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);

            //Then
            assertThat(applicationRequest.getExpenditure(), hasSize(1));
            assertThat(applicationRequest.getExpenditure(), contains(
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
            var dipApplicationRequest = getDIPCCApplicationRequestDto();
            var utilitiesExpenditure1 = getExpenditureDto("Utilities");
            var utilitiesExpenditure2 = getExpenditureDto("Utilities");
            utilitiesExpenditure2.setBalanceDeclared(null);
            utilitiesExpenditure2.setAmountDeclared(null);
            utilitiesExpenditure2.setPaymentVerified(null);
            utilitiesExpenditure2.setAmountVerified(null);

            dipApplicationRequest.setExpenditure(List.of(utilitiesExpenditure1, utilitiesExpenditure2));

            //When
            var applicationRequest = DIPCCApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);

            //Then
            assertThat(applicationRequest.getExpenditure(), hasSize(1));
            assertThat(applicationRequest.getExpenditure(), contains(
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
}