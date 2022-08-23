/*
 *   Copyright 2022 Selina Finance
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package com.selina.lending.internal.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.selina.lending.internal.dto.AdvancedLoanInformationDto;
import com.selina.lending.internal.dto.DIPApplicantDto;

class ApplicationDecisionResponseMapperTest extends MapperBase {

    @Test
    void mapToApplicationDecisionResponseDto() {
        //Given
        var applicationDecisionResponse = getApplicationDecisionResponse();

        //When
        var responseDto = ApplicationDecisionResponseMapper.INSTANCE.mapToApplicationDecisionResponseDto(applicationDecisionResponse);

        //Then
        assertThat(responseDto.getId(), equalTo(APPLICATION_ID));
        assertThat(responseDto.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(responseDto.getApplicants(), notNullValue());

        assertThat(responseDto.getOffers(), notNullValue());
        assertThat(responseDto.getOffers().size(), equalTo(1));
        assertThat(responseDto.getOffers().get(0).getProductCode(), equalTo(PRODUCT_CODE));
        assertThat(responseDto.getOffers().get(0).getRuleOutcomes(), notNullValue());
        assertThat(responseDto.getOffers().get(0).getRuleOutcomes().get(0).getOutcome(), equalTo(RULE_OUTCOME));
        assertThat(responseDto.getFees().getArrangementFee(), equalTo(ARRANGEMENT_FEE));

        assertThat(responseDto.getExpenditure(), notNullValue());
        assertThat(responseDto.getExpenditure().get(0).getExpenditureType(), equalTo(EXPENDITURE_TYPE));

        var dipApplicantDto = (DIPApplicantDto) responseDto.getApplicants().get(0);
        assertThat(dipApplicantDto.getFirstName(), equalTo(FIRST_NAME));
        assertThat(dipApplicantDto.getLivedInCurrentAddressFor3Years(), equalTo(true));
        assertThat(dipApplicantDto.getAddresses().size(), equalTo(1));
        assertThat(dipApplicantDto.getEstimatedRetirementAge(), equalTo(ESTIMATED_RETIREMENT_AGE));

        var advancedLoanInformationDto = (AdvancedLoanInformationDto) responseDto.getLoanInformation();
        assertThat(advancedLoanInformationDto.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(advancedLoanInformationDto.getFacilities(), notNullValue());
        assertThat(advancedLoanInformationDto.getFacilities().size(), equalTo(1));
        assertThat(advancedLoanInformationDto.getFacilities().get(0).getAllocationAmount(), equalTo(ALLOCATION_AMOUNT));
        assertThat(advancedLoanInformationDto.getFacilities().get(0).getAllocationPurpose(), equalTo(ALLOCATION_PURPOSE));
    }
}