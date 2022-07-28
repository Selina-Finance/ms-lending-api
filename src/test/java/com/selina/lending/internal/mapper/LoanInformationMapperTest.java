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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class LoanInformationMapperTest extends MapperBase{

    @Test
    public void mapToAdvancedLoanInformationDto() {
        //Given
        var loanInformation = getLoanInformation();

        //When
        var advancedLoanInformationDto = LoanInformationMapper.INSTANCE.mapToLoanInformationDto(loanInformation);

        //Then
        assertThat(advancedLoanInformationDto.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(advancedLoanInformationDto.getRequestedLoanAmount(), equalTo(LOAN_AMOUNT));
        assertThat(advancedLoanInformationDto.getRequestedLoanTerm(), equalTo(LOAN_TERM));
        assertThat(advancedLoanInformationDto.getDesiredTimeLine(), equalTo(DESIRED_TIME_LINE));
        assertThat(advancedLoanInformationDto.getNumberOfApplicants(), equalTo(1));
        assertThat(advancedLoanInformationDto.getFacilities().size(), equalTo(1));
        assertThat(advancedLoanInformationDto.getFacilities().get(0).getAllocationAmount(), equalTo(ALLOCATION_AMOUNT));
        assertThat(advancedLoanInformationDto.getFacilities().get(0).getAllocationPurpose(), equalTo(ALLOCATION_PURPOSE));
    }

    @Test
    public void mapToLoanInformationDto() {
        //Given
        var loanInformation = getLoanInformation();

        //When
        var loanInformationDto = LoanInformationMapper.INSTANCE.mapToLoanInformationDto(loanInformation);

        //Then
        assertThat(loanInformationDto.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(loanInformationDto.getRequestedLoanAmount(), equalTo(LOAN_AMOUNT));
        assertThat(loanInformationDto.getRequestedLoanTerm(), equalTo(LOAN_TERM));
        assertThat(loanInformationDto.getDesiredTimeLine(), equalTo(DESIRED_TIME_LINE));
        assertThat(loanInformationDto.getNumberOfApplicants(), equalTo(1));
    }

    @Test
    public void mapToLoanInformationFromAdvancedLoanInformationDto() {
        //Given
        var advancedLoanInformationDto = getAdvancedLoanInformationDto();

        //When
        var loanInformation = LoanInformationMapper.INSTANCE.mapToLoanInformation(advancedLoanInformationDto);

        //Then
        assertThat(loanInformation.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(loanInformation.getRequestedLoanAmount(), equalTo(LOAN_AMOUNT));
        assertThat(loanInformation.getRequestedLoanTerm(), equalTo(LOAN_TERM));
        assertThat(loanInformation.getDesiredTimeLine(), equalTo(DESIRED_TIME_LINE));
        assertThat(loanInformation.getNumberOfApplicants(), equalTo(1));
        assertThat(loanInformation.getFacilities().size(), equalTo(1));
        assertThat(loanInformation.getFacilities().get(0).getAllocationPurpose(), equalTo(ALLOCATION_PURPOSE));
        assertThat(loanInformation.getFacilities().get(0).getAllocationAmount(), equalTo(ALLOCATION_AMOUNT));
    }

    @Test
    public void mapToLoanInformationFromLoanInformationDto() {
        //Given
        var loanInformationDto = getLoanInformationDto();

        //When
        var loanInformation = LoanInformationMapper.INSTANCE.mapToLoanInformation(loanInformationDto);

        //Then
        assertThat(loanInformation.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(loanInformation.getRequestedLoanAmount(), equalTo(LOAN_AMOUNT));
        assertThat(loanInformation.getRequestedLoanTerm(), equalTo(LOAN_TERM));
        assertThat(loanInformation.getDesiredTimeLine(), equalTo(DESIRED_TIME_LINE));
        assertThat(loanInformation.getNumberOfApplicants(), equalTo(1));
        assertNull(loanInformation.getFacilities());
    }
}