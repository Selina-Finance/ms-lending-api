/*
 *  Copyright 2022 Selina Finance
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.selina.lending.internal.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class DIPApplicantDto extends ApplicantDto {

    @NotNull
    private Boolean applicantUsedAnotherName;

    @NotNull
    private Integer estimatedRetirementAge;

    @NotBlank
    private String maritalStatus;

    @NotBlank
    private String nationality;
    private String residentialStatus;

    @NotNull
    private Integer identifier;
    private IncomeDto income;

    @Valid
    @NotNull
    private EmploymentDto employment;
    private List<PreviousNameDto> previousNames;
}
