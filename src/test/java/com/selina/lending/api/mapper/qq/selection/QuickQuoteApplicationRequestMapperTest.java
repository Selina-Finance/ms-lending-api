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

package com.selina.lending.api.mapper.qq.selection;

import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.httpclient.selection.dto.request.Application;
import com.selina.lending.httpclient.selection.dto.request.Expenditure;
import com.selina.lending.httpclient.selection.dto.request.FilterQuickQuoteApplicationRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class QuickQuoteApplicationRequestMapperTest extends MapperBase {

    @Test
    void shouldMapToFilteredQuickQuoteApplicationRequest() {
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
        assertThat(applicationRequest.getApplication().getFees(), nullValue());

        assertPropertyDetails(application);
        assertPriorCharges(applicationRequest);
        assertExpenditures(application.getExpenditures());
    }

    @Test
    void shouldMapToFilteredQuickQuoteApplicationRequestWithNoPriorCharges() {
        //Given
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getPropertyDetails().setPriorCharges(null);

        //When
        var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

        //Then
        assertThat(applicationRequest.getApplication().getPropertyDetails(), notNullValue());
        assertPropertyDetails(applicationRequest.getApplication());
        assertThat(applicationRequest.getOptions().getPriorCharges(), nullValue());
        assertThat(applicationRequest.getApplication().getFees(), nullValue());
    }

    @Test
    void shouldMapToFilteredQuickQuoteApplicationRequestWithFees() {
        //Given
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();

        //When
        var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

        // Then
        assertFees(applicationRequest);
    }

    @Nested
    class MapExpenditureToMonthly {

        @Test
        void whenExpenditureFrequencyIsNotSpecifiedThenDefaultToMonthly() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getExpenditure().get(0).setFrequency(null);

            //When
            var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

            //Then
            Expenditure expenditure = applicationRequest.getApplication().getExpenditures().get(0);
            assertThat(expenditure.getFrequency(), equalTo("monthly"));
            assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED));
        }

        @Test
        void whenExpenditureFrequencyIsInvalidThenDefaultToMonthly() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getExpenditure().get(0).setFrequency("invalid-frequency");

            //When
            var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

            //Then
            Expenditure expenditure = applicationRequest.getApplication().getExpenditures().get(0);
            assertThat(expenditure.getFrequency(), equalTo("monthly"));
            assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED));
        }

        @Test
        void shouldMapDailyExpenditureToMonthly() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getExpenditure().get(0).setFrequency("daily");
            var dailyFrequencyFactor = 30;

            //When
            var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

            //Then
            Expenditure expenditure = applicationRequest.getApplication().getExpenditures().get(0);
            assertThat(expenditure.getFrequency(), equalTo("monthly"));
            assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED * dailyFrequencyFactor));
        }

        @Test
        void shouldMapWeeklyExpenditureToMonthly() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getExpenditure().get(0).setFrequency("weekly");
            var weeklyFrequencyFactor = 4.33;

            //When
            var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

            //Then
            Expenditure expenditure = applicationRequest.getApplication().getExpenditures().get(0);
            assertThat(expenditure.getFrequency(), equalTo("monthly"));
            assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED * weeklyFrequencyFactor));
        }

        @Test
        void shouldMapBiWeeklyExpenditureToMonthly() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getExpenditure().get(0).setFrequency("bi-weekly");
            var biWeeklyFrequencyFactor = 2.166;

            //When
            var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

            //Then
            Expenditure expenditure = applicationRequest.getApplication().getExpenditures().get(0);
            assertThat(expenditure.getFrequency(), equalTo("monthly"));
            assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED * biWeeklyFrequencyFactor));
        }

        @Test
        void shouldMapMonthlyExpenditureToMonthly() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getExpenditure().get(0).setFrequency("monthly");
            var monthlyFrequencyFactor = 1;

            //When
            var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

            //Then
            Expenditure expenditure = applicationRequest.getApplication().getExpenditures().get(0);
            assertThat(expenditure.getFrequency(), equalTo("monthly"));
            assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED * monthlyFrequencyFactor));
        }

        @Test
        void shouldMapQuarterlyExpenditureToMonthly() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getExpenditure().get(0).setFrequency("quarterly");
            var quarterlyFrequencyFactor = 0.25;

            //When
            var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

            //Then
            Expenditure expenditure = applicationRequest.getApplication().getExpenditures().get(0);
            assertThat(expenditure.getFrequency(), equalTo("monthly"));
            assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED * quarterlyFrequencyFactor));
        }

        @Test
        void shouldMapSemiAnnuallyExpenditureToMonthly() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getExpenditure().get(0).setFrequency("semi-annually");
            var semiAnnuallyFrequencyFactor = 0.125;

            //When
            var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

            //Then
            Expenditure expenditure = applicationRequest.getApplication().getExpenditures().get(0);
            assertThat(expenditure.getFrequency(), equalTo("monthly"));
            assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED * semiAnnuallyFrequencyFactor));
        }

        @Test
        void shouldMapAnnuallyExpenditureToMonthly() {
            //Given
            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getExpenditure().get(0).setFrequency("annually");
            var annuallyFrequencyFactor = 0.0833333;

            //When
            var applicationRequest = QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest);

            //Then
            Expenditure expenditure = applicationRequest.getApplication().getExpenditures().get(0);
            assertThat(expenditure.getFrequency(), equalTo("monthly"));
            assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED * annuallyFrequencyFactor));
        }
    }

    private void assertPropertyDetails(Application application) {
        var propertyDetails = application.getPropertyDetails();
        assertThat(propertyDetails, notNullValue());
        assertThat(propertyDetails.getEstimatedValue(), equalTo(ESTIMATED_VALUE));
        assertThat(propertyDetails.getPostcode(), equalTo(POSTCODE));
    }

    private void assertPriorCharges(FilterQuickQuoteApplicationRequest applicationRequest) {
        var priorCharges = applicationRequest.getOptions().getPriorCharges();
        assertThat(priorCharges.getNumberPriorCharges(), equalTo(1));
        assertThat(priorCharges.getMonthlyPayment(), equalTo(MONTHLY_PAYMENT));
        assertThat(priorCharges.getBalanceOutstanding(), equalTo(OUTSTANDING_BALANCE));
        assertThat(priorCharges.getBalanceConsolidated(), equalTo(BALANCE_CONSOLIDATED));
        assertThat(priorCharges.getOtherDebtPayments(), equalTo(OTHER_DEBT_PAYMENTS));
    }

    private void assertFees(FilterQuickQuoteApplicationRequest applicationRequest) {
        var fees = applicationRequest.getApplication().getFees();
        assertThat(fees.getCommissionFee(), equalTo(FEE));
        assertThat(fees.getAdviceFee(), equalTo(FEE));
        assertThat(fees.getThirdPartyFee(), equalTo(FEE));
        assertThat(fees.getValuationFee(), equalTo(FEE));
        assertThat(fees.getIsAddCommissionFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddAdviceFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddArrangementFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddValuationFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddThirdPartyFeeToLoan(), equalTo(true));
        assertThat(fees.getIsAddProductFeesToFacility(), equalTo(true));
        assertThat(fees.getIsAddIntermediaryFeeToLoan(), equalTo(false));
        assertThat(fees.getIsAddArrangementFeeSelinaToLoan(), equalTo(true));
        assertThat(fees.getArrangementFee(), equalTo(ARRANGEMENT_FEE));
    }

    private void assertExpenditures(List<Expenditure> expenditures) {
        assertThat(expenditures, hasSize(1));

        var expenditure = expenditures.get(0);
        assertThat(expenditure.getExpenditureType(), equalTo(EXPENDITURE_TYPE));
        assertThat(expenditure.getAmountDeclared(), equalTo(EXPENDITURE_AMOUNT_DECLARED));
        assertThat(expenditure.getFrequency(), equalTo(EXPENDITURE_FREQUENCY));
    }
}