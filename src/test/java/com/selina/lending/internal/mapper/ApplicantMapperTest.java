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

import org.junit.jupiter.api.Test;

import com.selina.lending.internal.dto.ApplicantDto;
import com.selina.lending.internal.dto.DIPApplicantDto;
import com.selina.lending.internal.service.application.domain.Applicant;

public class ApplicantMapperTest extends MapperBase {


    @Test
    void mapToApplicant() {
        //Given
        DIPApplicantDto dipApplicantDto = DIPApplicantDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .gender(GENDER)
                .emailAddress(EMAIL_ADDRESS)
                .mobileNumber(MOBILE_NUMBER)
                .identifier(1)
                .estimatedRetirementAge(ESTIMATED_RETIREMENT_AGE)
                .addresses(getAddressDtoList())
                .nationality(NATIONALITY)
                .applicant2LivesWithApplicant1(false)
                .dateOfBirth(DOB)
                .build();

        //When
        Applicant applicant = ApplicantMapper.INSTANCE.mapToApplicant(dipApplicantDto);

        //Then
        assertThat(FIRST_NAME, equalTo(applicant.getFirstName()));
        assertThat(LAST_NAME, equalTo(applicant.getLastName()));
        assertThat(GENDER, equalTo(applicant.getGender()));
        assertThat(EMAIL_ADDRESS, equalTo(applicant.getEmailAddress()));
        assertThat(MOBILE_NUMBER, equalTo(applicant.getMobilePhoneNumber()));
        assertThat(1, equalTo(applicant.getIdentifier()));
        assertThat(ESTIMATED_RETIREMENT_AGE, equalTo(applicant.getEstimatedRetirementAge()));
    }

    @Test
    void testMapToApplicant() {

        //Given
        ApplicantDto applicantDto = ApplicantDto.builder()
                .build();

        //When
        ApplicantMapper.INSTANCE.mapToApplicant(applicantDto);

        //Then
    }

    @Test
    void mapToApplicantDto() {
    }

}