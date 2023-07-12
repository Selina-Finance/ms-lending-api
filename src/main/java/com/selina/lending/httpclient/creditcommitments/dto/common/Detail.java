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

package com.selina.lending.httpclient.creditcommitments.dto.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Detail {
    private Integer id;
    private String status;
    private String applicant;
    private String name;
    private String dateOfBirth;
    private String category;
    private String startDate;
    private String endDate;
    private String lastUpdatedDate;
    private String settlementDate;
    private Integer remainingTerm;
    private Double outstandingBalance;
    private Double monthlyPayment;
    private Double creditLimit;
    private Boolean securityProperty;
    private String paymentProfileShort;
    private String paymentProfileLong;
    private Boolean joint;
    private Boolean adverseCredit;
    private Boolean consolidate;
    private Boolean ignore;
    private String repaymentType;
    private String interestOnlyBalance;
    private String repaymentVehicle;
    private String costOfRepaymentVehicle;
    private String interestRateType;
    private String fixedRatePeriodEndDate;
    private Double amountToConsolidate;
    private String reasonToIgnore;
    private String lender;
    private String type;
    private Integer amount;
    private Double currentBalance;
    private String code;
    private String date;
}
