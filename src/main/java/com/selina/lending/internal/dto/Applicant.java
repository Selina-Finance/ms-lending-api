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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class Applicant {
    @NotBlank(message = "title is required")
    private String title;

    @Email(message = "emailAddress is not valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotBlank(message = "emailAddress is required")
    private String emailAddress;

    @NotBlank(message = "mobileNumber is required")
    private String mobileNumber;

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;
    private String middleName;
    private String gender;

    @NotNull(message = "dateOfBirth is required")
    private Date dateOfBirth;
    private int numberOfAdultDependants;
    private int numberOfChildDependants;

    @NotNull(message = "livedInCurrrentAddressFor3Years is required")
    private Boolean livedInCurrentAddressFor3Years;
    private Boolean applicant2LivesWithApplicant1For3Years;
    private Boolean applicant2LivesWithApplicant1;

    private Date currentAddressMovedInDate;

    @NotNull(message = "address is required")
    private List<Address> addresses;
}
