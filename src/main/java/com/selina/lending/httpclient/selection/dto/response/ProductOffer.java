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

package com.selina.lending.httpclient.selection.dto.response;

import com.selina.lending.httpclient.middleware.dto.common.Erc;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOffer {
    private String id;
    private Double propertyValuation;
    private Double requestedLoanAmount;
    private Double maximumLoanAmount;
    private Double offerBalance;
    private Double aprc;
    private Boolean isAprcHeadline;
    private Double ear;
    private Double svr;
    private Double productFee;
    private Boolean hasFee;
    private Boolean canAddProductFee;
    private Double initialRate;
    private Double initialPayment;
    private Double reversionPayment;
    private Double totalAmountRepaid;
    private Double procFee;
    private Integer term;
    private Double lti;
    private Integer initialTerm;
    private Integer reversionTerm;
    private Double brokerFeesIncluded;
    private Double ltvCap;
    private Double ltvBand;
    private Double cltv;
    private Boolean hasProductFeeAddedToLoan;
    private String decision;
    private Double netLtv;
    private Double netLoanAmount;
    private Double maxErc;
    private Double brokerFeesUpfront;
    private Double affordabilityDeficit;
    private Double maximumBalanceEsis;
    private Integer ercPeriodYears;
    private Double eligibility;
    private String expiryDate;
    private Double dtir;
    private Double bbr;
    private Double initialMargin;
    private Double poundPaidPerBorrowed;
    private Double monthlyPaymentStressed;
    private Double baseRateStressed;
    private Double initialRateMinimum;
    private Double reversionMargin;
    private Double reversionRateMinimum;
    private Integer drawdownTerm;
    private Double esisLtvCappedBalance;
    private Double incomePrimaryApplicant;
    private Double incomeJointApplicant;
    private Integer daysUntilInitialDrawdown;
    private Integer fixedTermYears;
    private Double minimumInitialDrawdown;
    private Double offerValidity;
    private Double esisLoanAmount;
    private Double arrangementFeeSelina;
    private List<Erc> ercData;
}
