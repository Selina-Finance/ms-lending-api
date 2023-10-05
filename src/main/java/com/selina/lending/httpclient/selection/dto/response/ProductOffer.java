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
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class ProductOffer {
    String id;
    Double propertyValuation;
    Double requestedLoanAmount;
    Double maximumLoanAmount;
    Double offerBalance;
    Double aprc;
    Boolean isAprcHeadline;
    Double ear;
    Double svr;
    Double productFee;
    Boolean hasFee;
    Boolean canAddProductFee;
    Double initialRate;
    Double initialPayment;
    Double reversionPayment;
    Double totalAmountRepaid;
    Double procFee;
    Integer term;
    Double lti;
    Integer initialTerm;
    Integer reversionTerm;
    Double brokerFeesIncluded;
    Double ltvCap;
    Double cltv;
    Boolean hasProductFeeAddedToLoan;
    String decision;
    Double netLoanAmount;
    Double maxErc;
    Double brokerFeesUpfront;
    Double affordabilityDeficit;
    Double maximumBalanceEsis;
    Integer ercPeriodYears;
    Double eligibility;
    String expiryDate;
    Double dtir;
    Double bbr;
    Double initialMargin;
    Double poundPaidPerBorrowed;
    Double monthlyPaymentStressed;
    String decisionAml;
    String decisionFraud;
    Double baseRateStressed;
    Double aprcStressed;
    Double initialRateMinimum;
    Double reversionMargin;
    Double reversionRateMinimum;
    Integer drawdownTerm;
    Double esisLtvCappedBalance;
    Double incomePrimaryApplicant;
    Double incomeJointApplicant;
    Integer daysUntilInitialDrawdown;
    Integer fixedTermYears;
    Double minimumInitialDrawdown;
    Double offerValidity;
    Double esisLoanAmount;
    Double arrangementFeeSelina;
    List<Erc> ercData;
}
