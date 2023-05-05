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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Offer {
    String id;
    Boolean active;
    Boolean productFeeCanAdd;
    Double aprc;
    Double cltv;
    Double offerBalance;
    Integer initialTerm;
    Double initialRate;
    Double brokerFeesIncluded;
    Double reversionTerm;
    Double svr;
    Double procFee;
    Double lti;
    Double ltvCap;
    Double totalAmountRepaid;
    Double monthlyAffordabilityStatus;
    Double initialPayment;
    Double reversionPayment;
    Double affordabilityDeficit;
    String productCode;
    String product;
    String decision;
    Double productFee;
    Boolean productFeeAddedToLoan;
    Boolean selected;
    Double brokerFeesUpfront;
    Integer externalSettlement;
    @JsonProperty("eSignUrl")
    String esignUrl;
    Boolean hasFee;
    Checklist checklist;
    String plan;
    String tier;
    Integer maximumAdvance;
    Integer maximumAdvancePrime;
    Boolean isVariable;
    String family;
    String category;
    String variant;
    String ercShortcode;
    Integer ercPeriodYears;
    Double maximumBalanceEsis;
    Double maxErc;
    String ercProfile;
    List<Erc> ercData;
    List<RuleOutcome> ruleOutcomes;
    Double requestedLoanAmount;
    Double maximumLoanAmount;
    Integer term;
    Double ear;
    Boolean hasErc;
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
    Double propertyValuation;
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
}
