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
import lombok.Data;

@Builder
@Data
public class Detail {
    String id;
    String status;
    String applicant;
    String name;
    String dateOfBirth;
    String category;
    String startDate;
    String endDate;
    String lastUpdatedDate;
    String settlementDate;
    Integer remainingTerm;
    Double outstandingBalance;
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
    String fixedRatePeriodEndDate;
    Double amountToConsolidate;
    String reasonToIgnore;
    String lender;
    String type;
    Integer amount;
    Double currentBalance;
    String code;
}
