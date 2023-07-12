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

package com.selina.lending.api.support.validator;

import com.selina.lending.api.dto.dip.request.AdvancedLoanInformationDto;
import com.selina.lending.api.dto.dip.request.DIPApplicantDto;
import com.selina.lending.api.dto.dip.request.DIPApplicationRequest;
import com.selina.lending.api.dto.creditcommitments.request.ApplicantCreditCommitmentsDto;
import com.selina.lending.api.dto.creditcommitments.request.UpdateCreditCommitmentsRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class OnlyOnePrimaryApplicantImplTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Nested
    class ValidateApplicantCreditCommitmentDto {

        @Test
        void shouldReturnValidWhenOnlyOnePrimaryApplicant() {
            //Given
            var request = buildRequest(List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(true).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(false));
        }

        @Test
        void shouldReturnInvalidWhenPrimaryApplicantNotSet() {
            //Given
            var request = buildRequest(List.of(ApplicantCreditCommitmentsDto.builder().build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }

        @Test
        void shouldReturnInvalidWhenApplicantListNotSet() {
            //Given
            var request = UpdateCreditCommitmentsRequest.builder().build();

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }

        @Test
        void shouldReturnInvalidWhenNoPrimaryApplicant() {
            //Given
            var request = buildRequest(List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(false).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }

        @Test
        void shouldReturnValidIfOnlyOnePrimaryApplicantInList() {
            //Given
            var request = buildRequest(List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(true).build(),
                    ApplicantCreditCommitmentsDto.builder().primaryApplicant(false).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(false));
        }

        @Test
        void shouldReturnInvalidWhenNoPrimaryApplicantInList() {
            //Given
            var request = buildRequest(List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(false).build(),
                    ApplicantCreditCommitmentsDto.builder().primaryApplicant(false).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }

        @Test
        void shouldReturnInvalidWhenTwoPrimaryApplicantsInList() {
            //Given
            var request = buildRequest(List.of(ApplicantCreditCommitmentsDto.builder().primaryApplicant(true).build(),
                    ApplicantCreditCommitmentsDto.builder().primaryApplicant(true).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }

        private UpdateCreditCommitmentsRequest buildRequest(List<ApplicantCreditCommitmentsDto> applicants) {
            return UpdateCreditCommitmentsRequest.builder().applicants(applicants).build();
        }
    }

    @Nested
    class ValidateDIPApplicantDto {

        @Test
        void shouldReturnValidWhenOnlyOnePrimaryApplicant() {
            //Given
            var request = buildDIPApplicationRequest(List.of(DIPApplicantDto.builder().primaryApplicant(true).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(false));
        }

        @Test
        void shouldReturnInvalidWhenPrimaryApplicantNotSet() {
            //Given
            var request = buildDIPApplicationRequest(List.of(DIPApplicantDto.builder().build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }


        @Test
        void shouldReturnInvalidWhenApplicantListNotSet() {
            //Given
            var request = DIPApplicationRequest.builder().build();

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }


        @Test
        void shouldReturnInvalidWhenNoPrimaryApplicant() {
            //Given
            var request = buildDIPApplicationRequest(List.of(DIPApplicantDto.builder().primaryApplicant(false).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }

        @Test
        void shouldReturnValidIfOnlyOnePrimaryApplicantInList() {
            //Given
            var request = buildDIPApplicationRequest(List.of(DIPApplicantDto.builder().primaryApplicant(true).build(),
                    DIPApplicantDto.builder().primaryApplicant(false).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(false));
        }

        @Test
        void shouldReturnInvalidWhenNoPrimaryApplicantInList() {
            //Given
            var request = buildDIPApplicationRequest(List.of(DIPApplicantDto.builder().primaryApplicant(false).build(),
                    DIPApplicantDto.builder().primaryApplicant(false).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }

        @Test
        void shouldReturnInvalidWhenTwoPrimaryApplicantsInList() {
            //Given
            var request = buildDIPApplicationRequest(List.of(DIPApplicantDto.builder().primaryApplicant(true).build(),
                    DIPApplicantDto.builder().primaryApplicant(true).build()));

            //When
            var violations = validator.validate(request);

            //Then
            assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                    equalTo(true));
        }

        private DIPApplicationRequest buildDIPApplicationRequest(List<DIPApplicantDto> applicants) {
            return DIPApplicationRequest.builder().applicants(applicants).loanInformation(
                    AdvancedLoanInformationDto.builder().build()).build();
        }
    }
}