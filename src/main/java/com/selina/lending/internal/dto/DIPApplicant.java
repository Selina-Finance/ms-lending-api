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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class DIPApplicant extends Applicant {

    @NotBlank(message = "applicantUsedAnotherName is required")
    private Boolean applicantUsedAnotherName;

    @NotBlank(message = "estimatedRetirementAge is required")
    private Integer estimatedRetirementAge;

    @NotBlank(message = "maritalStatus is required")
    private String maritalStatus;

    @NotBlank(message = "nationality is required")
    private String nationality;
    private String residentialStatus;

    @NotBlank(message = "identifier is required")
    private Integer identifier;
    private Income income;
    @NotNull(message = "employment is required")
    private Employment employment;
    private List<PreviousName> previousNames;
}
