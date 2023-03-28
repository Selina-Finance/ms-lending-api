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

import static com.selina.lending.api.controller.SwaggerConstants.EMAIL_PATTERN;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.selina.lending.api.controller.SwaggerConstants;
import com.selina.lending.api.support.converter.ToLowerCase;
import com.selina.lending.api.support.validator.EnumValue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Data
public class ApplicantDto {
    @NotBlank
    @Schema(implementation = Title.class)
    @EnumValue(enumClass = Title.class)
    private String title;

    @Email(message = "emailAddress is not valid", regexp = EMAIL_PATTERN)
    @NotBlank
    @ToLowerCase
    private String emailAddress;

    @NotBlank
    @Size(min = 10, max = 13)
    private String mobileNumber;

    @NotBlank
    @Size(min = 2, max = 255)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 255)
    private String lastName;
    private String middleName;

    @Schema(implementation = Gender.class)
    @EnumValue(enumClass = Gender.class)
    private String gender;

    @NotNull
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
    private String dateOfBirth;
    private Integer numberOfAdultDependants;
    @Schema(description = "number of dependants under 16 years old")
    private Integer numberOfChildDependants;

    @Schema(implementation = ResidentialStatus.class, description = "If residential status value is not in the enum list, use best match e.g. Private Tenant map to 'Owner Occupier'")
    @EnumValue(enumClass = ResidentialStatus.class)
    private String residentialStatus;

    private Boolean applicant2LivesWithApplicant1;
    private Boolean applicant2LivesWithApplicant1For3Years;

    @NotNull
    @Valid
    @NotEmpty
    private List<AddressDto> addresses;

    @NotNull
    @Valid
    private IncomeDto income;

    enum Title {
        MR("Mr."),
        MRS("Mrs."),
        MISS("Miss"),
        MS("Ms."),
        DR("Dr."),
        OTHER("Other");

        final String value;

        Title(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum Gender {
        MALE("Male"),
        FEMALE("Female"),
        OTHER("Other");

        final String value;

        Gender(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum ResidentialStatus {
        OWNER("Owner"),
        OWNER_OCCUPIER("Owner Occupier");
        final String value;

        ResidentialStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
