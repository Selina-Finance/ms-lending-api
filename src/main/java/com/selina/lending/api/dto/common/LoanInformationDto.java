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

package com.selina.lending.api.dto.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.selina.lending.api.support.validator.EnumValue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Data
public class LoanInformationDto {
    @NotNull
    @Range(min = 10000, max = 1000000)
    @Schema(description = "the amount the applicant wishes to borrow (min 10000, max 1000000)")
    private Integer requestedLoanAmount;

    @NotNull
    @Range(min = 5, max = 30)
    @Schema(description = "the length of time in years for which the applicant wishes to repay the loan for (min 5, max 30)")
    private Integer requestedLoanTerm;

    @NotNull
    @Range(min = 1, max = 2)
    @Schema(description = "the number of applicants 1 or 2")
    private Integer numberOfApplicants;

    @NotBlank
    @Schema(implementation = LoanPurpose.class, description = "the purpose of which this applicant requires a loan")
    @EnumValue(enumClass = LoanPurpose.class)
    private String loanPurpose;
    private String desiredTimeLine;
}
