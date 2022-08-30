/*
 *   Copyright 2022 Selina Finance
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package com.selina.lending.internal.dto;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ApplicantResponseDto {
    String title;
    String emailAddress;
    String mobileNumber;
    String firstName;
    String lastName;
    String middleName;
    String gender;
    String dateOfBirth;
    int numberOfAdultDependants;
    int numberOfChildDependants;
    Boolean livedInCurrentAddressFor3Years;
    Boolean applicant2LivesWithApplicant1For3Years;
    Boolean applicant2LivesWithApplicant1;
    String currentAddressMovedInDate;
    Boolean applicantConsentedToSoftCreditCheck;
    Boolean hasAnyCriminalConvictions;
    Boolean hasBeenSubjectToDebtSolutionsInPart6Years;
    Boolean kqUkResidency;
    Boolean notHavePermanentRightToResideInTheUk;
    Boolean applicantUsedAnotherName;
    Integer estimatedRetirementAge;
    String maritalStatus;
    String nationality;
    List<AddressDto> addresses;
    IncomeDto income;
    EmploymentDto employment;
    List<PreviousNameDto> previousNames;
    CreditCheckDto creditCheck;
    ChecklistDto checklist;
    List<DocumentDto> documents;
}
