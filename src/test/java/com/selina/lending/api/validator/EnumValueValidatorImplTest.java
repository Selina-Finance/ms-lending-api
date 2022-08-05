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

package com.selina.lending.api.validator;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.selina.lending.internal.dto.ExpenditureDto;
import com.selina.lending.internal.dto.LoanInformationDto;

public class EnumValueValidatorImplTest {
    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    public void validateLoanInformation() {
        //Given
        var loanInformationDto =
                LoanInformationDto.builder()
                        .requestedLoanAmount(10000)
                        .requestedLoanTerm(3)
                        .numberOfApplicants(1)
                        .loanPurpose("invalid")
                        .build();

        //When
        var violations = validator.validate(loanInformationDto);

        //Then
        assertThat(violations.size(), equalTo(1));
        var violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("loanPurpose"));
        assertThat(violation.getMessage(), equalTo("value is not valid"));
        assertThat(violation.getInvalidValue(), equalTo("invalid"));
    }


    @Test
    public void validateExpenditure() {
        //Given
        var expenditure = ExpenditureDto.builder().expenditureType("invalid").build();

        //When
        var violations = validator.validate(expenditure);

        //Then
        assertThat(violations.size(), equalTo(1));
        var violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("expenditureType"));
        assertThat(violation.getMessage(), equalTo("value is not valid"));
        assertThat(violation.getInvalidValue(), equalTo("invalid"));
    }
}