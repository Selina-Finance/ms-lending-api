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

import com.selina.lending.api.mapper.MapperBase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class DIPApplicantMapperTest extends MapperBase {

    @Test
    void mapToApplicantFromDIPApplicantDto() {
        //Given
        var dipApplicantDto = getDIPApplicantDto();

        //When
        var applicant = DIPApplicantMapper.INSTANCE.mapToApplicant(dipApplicantDto);

        //Then
        assertThat(applicant.getTitle(), equalTo(TITLE));
        assertThat(applicant.getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicant.getLastName(), equalTo(LAST_NAME));
        assertThat(applicant.getGender(), equalTo(GENDER));
        assertThat(applicant.getApplicantUsedAnotherName(), equalTo(false));
        assertThat(applicant.getEmailAddress(), equalTo(EMAIL_ADDRESS));
        assertThat(applicant.getNationality(), equalTo(NATIONALITY));
        assertThat(applicant.getMobilePhoneNumber(), equalTo(MOBILE_NUMBER));
        assertThat(applicant.getDateOfBirth(), equalTo(DOB));
        assertThat(applicant.getAddresses().size(), equalTo(1));
        assertThat(applicant.getEstimatedRetirementAge(), equalTo(ESTIMATED_RETIREMENT_AGE));
        assertThat(applicant.getEmployment().getEmployerName(), equalTo(EMPLOYER_NAME));
        assertThat(applicant.getIncome().getIncome().size(), equalTo(1));
        assertThat(applicant.getIncome().getIncome().get(0).getAmount(), equalTo(INCOME_AMOUNT));
        assertThat(applicant.getIncome().getIncome().get(0).getType(), equalTo(INCOME_TYPE));
        assertThat(applicant.getPreviousNames().size(), equalTo(1));
        assertThat(applicant.getPreviousNames().get(0).getTitle(), equalTo(TITLE));
        assertThat(applicant.getPreviousNames().get(0).getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicant.getPreviousNames().get(0).getLastName(), equalTo(LAST_NAME));
    }

    @Test
    void mapToApplicantDto() {
        //Given
        var applicant = getApplicant();

        //When
        var applicantDto = DIPApplicantMapper.INSTANCE.mapToApplicantDto(applicant);

        //Then
        assertThat(applicantDto.getTitle(), equalTo(TITLE));
        assertThat(applicantDto.getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicantDto.getLastName(), equalTo(LAST_NAME));
        assertThat(applicantDto.getGender(), equalTo(GENDER));
        assertThat(applicantDto.getEmailAddress(), equalTo(EMAIL_ADDRESS));
        assertThat(applicantDto.getMobileNumber(), equalTo(MOBILE_NUMBER));
        assertThat(applicantDto.getDateOfBirth(), equalTo(DOB));
        assertThat(applicantDto.getAddresses().size(), equalTo(1));
    }

    @Test
    void mapToDipApplicantDto() {
        //Given
        var applicant = getApplicant();

        //When
        var dipApplicantDto = DIPApplicantMapper.INSTANCE.mapToApplicantDto(applicant);

        //Then
        assertThat(dipApplicantDto.getTitle(), equalTo(TITLE));
        assertThat(dipApplicantDto.getFirstName(), equalTo(FIRST_NAME));
        assertThat(dipApplicantDto.getLastName(), equalTo(LAST_NAME));
        assertThat(dipApplicantDto.getGender(), equalTo(GENDER));
        assertThat(dipApplicantDto.getNationality(), equalTo(NATIONALITY));
        assertThat(dipApplicantDto.getEmailAddress(), equalTo(EMAIL_ADDRESS));
        assertThat(dipApplicantDto.getMobileNumber(), equalTo(MOBILE_NUMBER));
        assertThat(dipApplicantDto.getDateOfBirth(), equalTo(DOB));
        assertThat(dipApplicantDto.getPrimaryApplicant(), equalTo(true));
        assertThat(dipApplicantDto.getEstimatedRetirementAge(), equalTo(ESTIMATED_RETIREMENT_AGE));
        assertThat(dipApplicantDto.getAddresses().size(), equalTo(1));
        assertThat(dipApplicantDto.getEmployment().getEmployerName(), equalTo(EMPLOYER_NAME));
        assertThat(dipApplicantDto.getIncome(), notNullValue());
        assertThat(dipApplicantDto.getIncome().getIncome().get(0).getAmount(), equalTo(INCOME_AMOUNT));
    }
}