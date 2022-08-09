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

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.selina.lending.api.validator.EnumValue;

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

    @Email(message = "emailAddress is not valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotBlank
    private String emailAddress;

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
    private String middleName;

    @Schema(implementation = Gender.class)
    @EnumValue(enumClass = Gender.class)
    private String gender;

    @NotNull
    private Date dateOfBirth;
    private int numberOfAdultDependants;
    private int numberOfChildDependants;

    @NotNull
    private Boolean livedInCurrentAddressFor3Years;
    private Boolean applicant2LivesWithApplicant1For3Years;
    private Boolean applicant2LivesWithApplicant1;

    private Date currentAddressMovedInDate;

    @NotNull
    @Valid
    @NotEmpty
    private List<AddressDto> addresses;

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
}
