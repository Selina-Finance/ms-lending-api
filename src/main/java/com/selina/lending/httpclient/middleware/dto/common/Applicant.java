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

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class Applicant {
    private String title;
    private String emailAddress;
    private String mobilePhoneNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private String dateOfBirth;
    private Integer numberOfAdultDependants;
    private Integer numberOfChildDependants;
    private Boolean applicantUsedAnotherName;
    private Integer estimatedRetirementAge;
    private String maritalStatus;
    private String nationality;
    private String residentialStatus;
    private Boolean primaryApplicant;
    private Integer identifier;
    private Incomes income;
    private Employment employment;
    private List<PreviousName> previousNames;
    private Boolean applicant2LivesWithApplicant1;
    private Boolean applicant2LivesWithApplicant1For3Years;
    private List<Address> addresses;
    private CreditCheck creditCheck;
    private Checklist checklist;
    private List<Document> documents;
    private Boolean applicantConsentedToSoftCreditCheck;
    private Boolean hasBeenSubjectToDebtSolutionsInPart6Years;
    private Boolean kqUkResidency;
    private Boolean notHavePermanentRightToResideInTheUk;
    private Boolean hasAnyCriminalConvictions;
}
