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

package com.selina.lending.internal.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.selina.lending.api.support.validator.OnlyOnePrimaryApplicant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
public class DIPCCApplicationRequest extends ApplicationRequest {
    @NotNull
    private String sourceClientId;

    @NotNull
    @Size(message = "applicants is required", min = 1, max = 2)
    @Valid
    @OnlyOnePrimaryApplicant
    private List<DIPApplicantDto> applicants;

    @NotNull
    @Valid
    private AdvancedLoanInformationDto loanInformation;

    @NotNull
    @Valid
    private DIPCCPropertyDetailsDto propertyDetails;

    @Valid
    private FeesDto fees;
}
