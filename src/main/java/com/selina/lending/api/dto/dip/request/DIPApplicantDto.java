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

import com.selina.lending.api.dto.common.ApplicantDto;
import com.selina.lending.api.dto.common.EmploymentDto;
import com.selina.lending.api.dto.common.PreviousNameDto;
import com.selina.lending.api.validator.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    @Schema(implementation = MaritalStatus.class)
    @EnumValue(enumClass = MaritalStatus.class)
    private String maritalStatus;

    @NotBlank
    @Schema(implementation = Nationality.class)
    @EnumValue(enumClass = Nationality.class)
    private String nationality;

    @NotNull
    private Boolean primaryApplicant;

    @Valid
    @NotNull
    private EmploymentDto employment;

    @Valid
    private List<PreviousNameDto> previousNames;

    enum MaritalStatus {
        SINGLE("Single"),
        MARRIED("Married"),
        CIVIL_PARTNERSHIP("Civil partnership"),
        WIDOWED("Widowed"),
        DIVORCED("Divorced"),
        SEPARATED("Separated");

        private final String value;

        MaritalStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
