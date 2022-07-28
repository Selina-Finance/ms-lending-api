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

package com.selina.lending.internal.service.application.domain;

import java.util.Date;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Detail {
    String id;
    String status;
    String applicant;
    String category;
    Date startDate;
    Date endDate;
    Date lastUpdatedDate;
    Integer remainingTerm;
    Double currentBalance;
    Double monthlyPayment;
    Double creditLimit;
    Boolean securityProperty;
    String paymentProfileShort;
    String paymentProfileLong;
    Boolean adverseCredit;
    Boolean consolidate;
    Boolean ignore;
    String repaymentType;
    String interestOnlyBalance;
    String repaymentVehicle;
    String costOfRepaymentVehicle;
    String interestRateType;
    Date fixedRatePeriodEndDate;
    Double amountToConsolidate;
    String reasonToIgnore;
    String lender;
    String accountNumber;
    String type;
    String date;
    Integer amount;
}
