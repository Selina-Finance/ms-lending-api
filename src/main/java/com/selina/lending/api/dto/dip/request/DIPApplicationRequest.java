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

package com.selina.lending.api.dto.dip.request;

import static com.selina.lending.api.controller.SwaggerConstants.EMAIL_PATTERN;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.selina.lending.api.converter.ToLowerCase;
import com.selina.lending.api.validator.MatchNumberOfApplicants;
import com.selina.lending.api.validator.OnlyOnePrimaryApplicant;

import com.selina.lending.api.validator.SecondApplicantHasRequiredValues;
import com.selina.lending.api.dto.common.FeesDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@MatchNumberOfApplicants
public class DIPApplicationRequest extends ApplicationRequest {
    @Schema(description = "the email address of the broker submitting the application. They should have an account on our Broker Portal")
    @Email(message = "brokerSubmitterEmail is not valid", regexp = EMAIL_PATTERN)
    @ToLowerCase
    private String brokerSubmitterEmail;

    @NotNull
    @Size(min = 4, max = 255)
    private String sourceClientId;

    @NotNull
    @Size(message = "applicants is required", min = 1, max = 2)
    @Valid
    @OnlyOnePrimaryApplicant
    @SecondApplicantHasRequiredValues
    private List<DIPApplicantDto> applicants;

    @NotNull
    @Valid
    private AdvancedLoanInformationDto loanInformation;

    @NotNull
    @Valid
    private DIPPropertyDetailsDto propertyDetails;

    @Valid
    private FeesDto fees;
}
