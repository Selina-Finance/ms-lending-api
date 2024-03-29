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

package com.selina.lending.api.validator;

import com.selina.lending.api.dto.creditcommitments.request.ApplicantCreditCommitmentsDto;
import com.selina.lending.api.dto.creditcommitments.request.CreditCommitmentsDetailDto;
import com.selina.lending.api.dto.creditcommitments.request.DetailDto;
import com.selina.lending.api.dto.creditcommitments.request.SystemDto;
import com.selina.lending.api.dto.creditcommitments.request.UpdateCreditCommitmentsRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CCReasonToIgnoreValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldInvalidateWhenIgnoreButReasonIsAbsent() {
        //Given
        var updateCCRequest = UpdateCreditCommitmentsRequest.builder().applicants(
                List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(true)
                        .creditCommitments(CreditCommitmentsDetailDto.builder()
                                .system(SystemDto.builder()
                                        .detail(List.of(DetailDto.builder().id(1).ignore(true).build()))
                                        .build())
                                .build())
                        .build())).build();

        //When
        var violations = validator.validate(updateCCRequest);

        //Then
        assertThat(violations.size(), equalTo(1));

        var violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("applicants[0].creditCommitments.system.detail[0].reasonToIgnore"));
        assertThat(violation.getMessage(), equalTo("This field is required"));
    }

    @Test
    void shouldInvalidateWhenIgnoreButReasonIsNotAcceptable() {
        //Given
        var updateCCRequest = UpdateCreditCommitmentsRequest.builder().applicants(
                List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(true)
                        .creditCommitments(CreditCommitmentsDetailDto.builder()
                                .system(SystemDto.builder().detail(List.of(DetailDto.builder().id(1)
                                        .ignore(true)
                                        .reasonToIgnore("bla-bla")
                                        .build())).build())
                                .build())
                        .build())).build();

        //When
        var violations = validator.validate(updateCCRequest);

        //Then
        assertThat(violations.size(), equalTo(1));

        var violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("applicants[0].creditCommitments.system.detail[0].reasonToIgnore"));
        assertThat(violation.getMessage(), equalTo("value is not valid"));
    }

    @Test
    void shouldBeValidWhenIgnoreAndReasonAcceptable() {
        //Given
        var updateCCRequest = UpdateCreditCommitmentsRequest.builder().applicants(
                List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(true)
                        .creditCommitments(CreditCommitmentsDetailDto.builder()
                                .system(SystemDto.builder().detail(List.of(DetailDto.builder().id(1)
                                        .ignore(true)
                                        .reasonToIgnore("Item is disputed - Experian to be updated")
                                        .build())).build())
                                .build())
                        .build())).build();

        //When
        var violations = validator.validate(updateCCRequest);

        //Then
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    void shouldBeValidWhenDoNotPassIgnoreAndReasonToIgnore() {
        //Given
        var updateCCRequest = UpdateCreditCommitmentsRequest.builder().applicants(
                List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(true).build())).build();

        //When
        var violations = validator.validate(updateCCRequest);

        //Then
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    void shouldBeValidWhenPassIgnoreAsFalse() {
        //Given
        var updateCCRequest = UpdateCreditCommitmentsRequest.builder().applicants(
                List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(true)
                        .creditCommitments(CreditCommitmentsDetailDto.builder()
                                .system(SystemDto.builder()
                                        .detail(List.of(DetailDto.builder().id(1).ignore(false).build()))
                                        .build())
                                .build())
                        .build())).build();

        //When
        var violations = validator.validate(updateCCRequest);

        //Then
        assertThat(violations.size(), equalTo(0));
    }
}
