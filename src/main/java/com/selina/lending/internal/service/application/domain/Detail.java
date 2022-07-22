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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Detail {
    public String id;
    public String status;
    public String applicant;
    public String category;
    public Date startDate;
    public Date endDate;
    public Date lastUpdatedDate;
    public Integer remainingTerm;
    public Double currentBalance;
    public Double monthlyPayment;
    public Double creditLimit;
    public Boolean securityProperty;
    public String paymentProfileShort;
    public String paymentProfileLong;
    public Boolean adverseCredit;
    public Boolean consolidate;
    public Boolean ignore;
    public String repaymentType;
    public String interestOnlyBalance;
    public String repaymentVehicle;
    public String costOfRepaymentVehicle;
    public String interestRateType;
    public Date fixedRatePeriodEndDate;
    public Double amountToConsolidate;
    public String reasonToIgnore;
    public String lender;
    public String accountNumber;
    public String type;
    public String date;
    public Integer amount;
}
