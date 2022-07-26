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

import com.selina.lending.internal.dto.ApplicantDto;
import com.selina.lending.internal.dto.DIPApplicantDto;
import com.selina.lending.internal.service.application.domain.Applicant;

public class ApplicantMapperTest extends MapperBase {

    @Test
    public void mapToApplicantFromDIPApplicantDto() {
        //Given
        DIPApplicantDto dipApplicantDto = getDIPApplicantDto();

        //When
        Applicant applicant = ApplicantMapper.INSTANCE.mapToApplicant(dipApplicantDto);

        //Then
        assertThat(FIRST_NAME, equalTo(applicant.getFirstName()));
        assertThat(LAST_NAME, equalTo(applicant.getLastName()));
        assertThat(GENDER, equalTo(applicant.getGender()));
        assertThat(false, equalTo(applicant.getApplicantUsedAnotherName()));
        assertThat(EMAIL_ADDRESS, equalTo(applicant.getEmailAddress()));
        assertThat(MOBILE_NUMBER, equalTo(applicant.getMobilePhoneNumber()));
        assertThat(1, equalTo(applicant.getAddresses().size()));
        assertThat(1, equalTo(applicant.getIdentifier()));
        assertThat(ESTIMATED_RETIREMENT_AGE, equalTo(applicant.getEstimatedRetirementAge()));
        assertThat(true, equalTo(applicant.getLivedInCurrentAddressFor3Years()));
        assertThat(EMPLOYER_NAME, equalTo(applicant.getEmployment().getEmployerName()));
        assertThat(1, equalTo(applicant.getIncome().getIncome().size()));
        assertThat(INCOME_AMOUNT, equalTo(applicant.getIncome().getIncome().get(0).getAmount()));
        assertThat(INCOME_TYPE, equalTo(applicant.getIncome().getIncome().get(0).getType()));
    }

    @Test
    public void mapToApplicantFromApplicantDto() {
        //Given
        ApplicantDto applicantDto = getApplicantDto();

        //When
        Applicant applicant = ApplicantMapper.INSTANCE.mapToApplicant(applicantDto);

        //Then
        assertThat(FIRST_NAME, equalTo(applicant.getFirstName()));
        assertThat(LAST_NAME, equalTo(applicant.getLastName()));
        assertThat(GENDER, equalTo(applicant.getGender()));
        assertThat(EMAIL_ADDRESS, equalTo(applicant.getEmailAddress()));
        assertThat(MOBILE_NUMBER, equalTo(applicant.getMobilePhoneNumber()));
        assertThat(1, equalTo(applicant.getAddresses().size()));
        assertNull(applicant.getIdentifier());
        assertNull(applicant.getEstimatedRetirementAge());
        assertNull(applicant.getLivedInCurrentAddressFor3Years());
        assertNull(applicant.getEmployment());
        assertNull(applicant.getIncome());
    }

    @Test
    public void mapToApplicantDto() {
        //Given
        Applicant applicant = getApplicant();

        //When
        ApplicantDto applicantDto = ApplicantMapper.INSTANCE.mapToApplicantDto(applicant);

        //Then
        assertThat(FIRST_NAME, equalTo(applicantDto.getFirstName()));
        assertThat(LAST_NAME, equalTo(applicantDto.getLastName()));
        assertThat(GENDER, equalTo(applicantDto.getGender()));
        assertThat(EMAIL_ADDRESS, equalTo(applicantDto.getEmailAddress()));
        assertThat(MOBILE_NUMBER, equalTo(applicantDto.getMobileNumber()));
        assertThat(1, equalTo(applicantDto.getAddresses().size()));
    }

    @Test
    public void mapToDipApplicantDto() {
        //Given
        Applicant applicant = getApplicant();

        //When
        DIPApplicantDto dipApplicantDto = ApplicantMapper.INSTANCE.mapToApplicantDto(applicant);

        //Then
        assertThat(FIRST_NAME, equalTo(dipApplicantDto.getFirstName()));
        assertThat(LAST_NAME, equalTo(dipApplicantDto.getLastName()));
        assertThat(GENDER, equalTo(dipApplicantDto.getGender()));
        assertThat(EMAIL_ADDRESS, equalTo(dipApplicantDto.getEmailAddress()));
        assertThat(MOBILE_NUMBER, equalTo(dipApplicantDto.getMobileNumber()));
        assertThat(1, equalTo(dipApplicantDto.getIdentifier()));
        assertThat(ESTIMATED_RETIREMENT_AGE, equalTo(dipApplicantDto.getEstimatedRetirementAge()));
        assertThat(true, equalTo(dipApplicantDto.getLivedInCurrentAddressFor3Years()));
        assertThat(1, equalTo(dipApplicantDto.getAddresses().size()));
        assertThat(EMPLOYER_NAME, equalTo(dipApplicantDto.getEmployment().getEmployerName()));
    }
}