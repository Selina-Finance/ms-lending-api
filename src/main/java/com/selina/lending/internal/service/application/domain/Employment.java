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

package com.selina.lending.internal.service.application.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Employment {

    String employmentStatus;
    Boolean inProbationPeriod;
    String employmentType;
    String employerPhoneNumber;
    String postcode;
    String addressLine1;
    String addressLine2;
    String city;
    String contractStartDate;
    String contractEndDate;
    Boolean firstTimeContractor;
    String employerName;
    String jobTitle;
    String occupationType;
    Boolean isEmployedByFamilyMember;
    Boolean ownSharesInThisCompany;
    Double shareHolding;
    String registeredCompanyName;
    String selfEmployed;
    String fiscalYearReportedIncomeRelatesTo;
    String lengthSelfEmployed;
    String companyRegistrationNumber;
    String percentageOfCompanyOwned;
    String monthAccountingPeriodStarts;
    String whenWasCompanyIncorporated;
    String whenDidYouBeginTrading;
    String startDate;
    String partnershipFormedDate;
    Double percentageOfPartnershipOwned;
    String businessStructure;
    String industry;
}
