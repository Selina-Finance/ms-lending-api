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

package com.selina.lending.internal.service.application.dto;

import java.util.Date;
import java.util.List;

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
    private String title;
    private String emailAddress;
    private String mobilePhoneNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private Date dateOfBirth;
    private int numberOfAdultDependants;
    private int numberOfChildDependants;
    private Boolean livedInCurrentAddressFor3Years;
    private Boolean applicant2LivesWithApplicant1For3Years;
    private Boolean applicant2LivesWithApplicant1;
    private Date currentAddressMovedInDate;
    private Boolean applicantUsedAnotherName;
    private Integer estimatedRetirementAge;
    private String maritalStatus;
    private String nationality;
    private String residentialStatus;
    private Integer identifier;
    private Incomes income;
    private Employment employment;
    private List<PreviousName> previousNames;
    private List<Address> addresses;
    private Hooyu hooyu;
    private CreditCheck creditCheck;
    private Checklist checklist;
    private List<Document>documents;
    private Boolean applicantConsentedToSoftCreditCheck;
    private Boolean hasBeenSubjectToDebtSolutionsInPart6Years;
    private Boolean kqUkResidency;
    private Boolean notHavePermanentRightToResideInTheUk;
}
