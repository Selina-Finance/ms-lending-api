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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.selina.lending.internal.dto.ApplicationRequest;
import com.selina.lending.internal.dto.EmploymentDto;
import com.selina.lending.internal.dto.ExpenditureDto;
import com.selina.lending.internal.dto.IncomeDto;
import com.selina.lending.internal.dto.IncomeItemDto;
import com.selina.lending.internal.dto.LoanInformationDto;

public class EnumValueValidatorImplTest {
    private static final String INVALID_VALUE = "invalid";
    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void validateLoanInformation() {
        //Given
        var loanInformationDto = LoanInformationDto.builder()
                .requestedLoanAmount(10000)
                .requestedLoanTerm(3)
                .numberOfApplicants(1)
                .loanPurpose(INVALID_VALUE)
                .build();

        //When
        var violations = validator.validate(loanInformationDto);

        //Then
        assertThat(violations.size(), equalTo(1));
        var violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("loanPurpose"));
        assertThat(violation.getMessage(), equalTo("value is not valid"));
        assertThat(violation.getInvalidValue(), equalTo(INVALID_VALUE));
    }

    @Test
    public void validateExpenditure() {
        //Given
        var expenditure = ExpenditureDto.builder().expenditureType(INVALID_VALUE).frequency(INVALID_VALUE).build();

        //When
        var violations = validator.validate(expenditure);

        //Then
        assertThat(violations.size(), equalTo(2));

        var violationPropertyPath = getViolationPropertyPath(
                violations.stream().map(ConstraintViolation::getPropertyPath));

        assertThat(violationPropertyPath, containsInAnyOrder("expenditureType", "frequency"));
    }

    @Test
    public void validateApplicationRequest() {
        //Given
        var applicationRequest = ApplicationRequest.builder()
                .requestType(INVALID_VALUE)
                .source(INVALID_VALUE)
                .productCode(INVALID_VALUE)
                .build();

        //When
        var violations = validator.validate(applicationRequest);

        //Then
        assertThat(violations.size(), equalTo(3));

        var violationPropertyPath = getViolationPropertyPath(
                violations.stream().map(ConstraintViolation::getPropertyPath));

        assertThat(violationPropertyPath, containsInAnyOrder("requestType", "source", "productCode"));
    }

    @Test
    public void validateIncome() {
        //Given
        var income = IncomeDto.builder().income(List.of(IncomeItemDto.builder().type(INVALID_VALUE).build())).build();

        //When
        var violations = validator.validate(income);

        //Then
        assertThat(violations.size(), equalTo(1));

        var violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("income[0].type"));
        assertThat(violation.getMessage(), equalTo("value is not valid"));
        assertThat(violation.getInvalidValue(), equalTo(INVALID_VALUE));

    }

    @Test
    public void validateEmployment() {
        //Given
        var employment = EmploymentDto.builder()
                .employmentStatus(INVALID_VALUE)
                .employmentType(INVALID_VALUE)
                .selfEmployed(INVALID_VALUE)
                .lengthSelfEmployed(INVALID_VALUE)
                .build();

        //When
        var violations = validator.validate(employment);

        //Then
        assertThat(violations.size(), equalTo(4));

        var violationPropertyPath = getViolationPropertyPath(
                violations.stream().map(ConstraintViolation::getPropertyPath));

        assertThat(violationPropertyPath,
                containsInAnyOrder("employmentStatus", "employmentType", "selfEmployed", "lengthSelfEmployed"));
    }

    private List<String> getViolationPropertyPath(Stream<Path> violations) {
        return violations.toList().stream().map(Path::toString).collect(Collectors.toList());
    }
}