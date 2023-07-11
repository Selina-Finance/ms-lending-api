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

package com.selina.lending.httpclient.middleware.dto.common;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Applicant {
    String title;
    String emailAddress;
    String mobilePhoneNumber;
    String firstName;
    String lastName;
    String middleName;
    String gender;
    String dateOfBirth;
    Integer numberOfAdultDependants;
    Integer numberOfChildDependants;
    Boolean applicantUsedAnotherName;
    Integer estimatedRetirementAge;
    String maritalStatus;
    String nationality;
    String residentialStatus;
    Boolean primaryApplicant;
    Integer identifier;
    Incomes income;
    Employment employment;
    List<PreviousName> previousNames;
    Boolean applicant2LivesWithApplicant1;
    Boolean applicant2LivesWithApplicant1For3Years;
    List<Address> addresses;
    CreditCheck creditCheck;
    Checklist checklist;
    List<Document> documents;
    Boolean applicantConsentedToSoftCreditCheck;
    Boolean hasBeenSubjectToDebtSolutionsInPart6Years;
    Boolean kqUkResidency;
    Boolean notHavePermanentRightToResideInTheUk;
    Boolean hasAnyCriminalConvictions;
}
