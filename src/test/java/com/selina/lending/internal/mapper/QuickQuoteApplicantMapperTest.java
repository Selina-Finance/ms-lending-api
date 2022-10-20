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
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QuickQuoteApplicantMapperTest extends MapperBase{

    @Test
    void mapToApplicantFromQuickQuoteApplicantDto() {
        //Given
        var applicantDto = getQuickQuoteApplicantDto();

        //When
        var applicant = QuickQuoteApplicantMapper.INSTANCE.mapToApplicant(applicantDto);

        //Then
        assertThat(applicant.getTitle(), equalTo(TITLE));
        assertThat(applicant.getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicant.getLastName(), equalTo(LAST_NAME));
        assertThat(applicant.getGender(), equalTo(GENDER));
        assertThat(applicant.getEmailAddress(), equalTo(EMAIL_ADDRESS));
        assertThat(applicant.getMobilePhoneNumber() ,equalTo(MOBILE_NUMBER));
        assertThat(applicant.getDateOfBirth(), equalTo(DOB));
        assertThat(applicant.getAddresses().size(), equalTo(1));
        assertThat(applicant.getLivedInCurrentAddressFor3Years(), equalTo(true));
        assertThat(applicant.getIncome().getIncome().size(), equalTo(1));
        assertThat(applicant.getIncome().getIncome().get(0).getAmount(), equalTo(INCOME_AMOUNT));
        assertThat(applicant.getIncome().getIncome().get(0).getType(), equalTo(INCOME_TYPE));
        assertThat(applicant.getEmployment().getEmployerName(), equalTo(EMPLOYER_NAME));
        assertThat(applicantDto.getResidentialStatus(), equalTo(RESIDENTIAL_STATUS_OWNER));
        assertNull(applicant.getIdentifier());
        assertNull(applicant.getEstimatedRetirementAge());
    }

    @Test
    void mapToApplicantDto() {
        //Given
        var applicant = getApplicant();

        //When
        var applicantDto = QuickQuoteApplicantMapper.INSTANCE.mapToApplicantDto(applicant);

        //Then
        assertThat(applicantDto.getTitle(), equalTo(TITLE));
        assertThat(applicantDto.getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicantDto.getLastName(), equalTo(LAST_NAME));
        assertThat(applicantDto.getGender(), equalTo(GENDER));
        assertThat(applicantDto.getEmailAddress(), equalTo(EMAIL_ADDRESS));
        assertThat(applicantDto.getMobileNumber() ,equalTo(MOBILE_NUMBER));
        assertThat(applicantDto.getDateOfBirth(), equalTo(DOB));
        assertThat(applicantDto.getAddresses().size(), equalTo(1));
        assertThat(applicantDto.getEmployment().getEmployerName(), equalTo(EMPLOYER_NAME));
    }
}