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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class Offer {
    private String id;
    private Boolean active;
    private Boolean productFeeCanAdd;
    private Double aprc;
    private Double cltv;
    private Double offerBalance;
    private Integer initialTerm;
    private Double initialRate;
    private Double brokerFeesIncluded;
    private Double reversionTerm;
    private Double svr;
    private Double procFee;
    private Double lti;
    private Double ltvCap;
    private Double totalAmountRepaid;
    private Double monthlyAffordabilityStatus;
    private Double initialPayment;
    private Double reversionPayment;
    private Double affordabilityDeficit;
    private String productCode;
    private String product;
    private String decision;
    private Double productFee;
    private Boolean productFeeAddedToLoan;
    private Boolean selected;
    private Double brokerFeesUpfront;
    private Integer externalSettlement;
    @JsonProperty("eSignUrl")
    private String esignUrl;
    private Boolean hasFee;
    private Checklist checklist;
    private String plan;
    private String tier;
    private Integer maximumAdvance;
    private Integer maximumAdvancePrime;
    private Boolean isVariable;
    private String family;
    private String category;
    private String variant;
    private String ercShortcode;
    private Integer ercPeriodYears;
    private Double maximumBalanceEsis;
    private Double netLoanAmount;
    private Double maxErc;
    private String ercProfile;
    private List<Erc> ercData;
    private List<RuleOutcome> ruleOutcomes;
    private Double requestedLoanAmount;
    private Double maximumLoanAmount;
    private Integer term;
    private Double ear;
    private Boolean hasErc;
    private String expiryDate;
    private Double dtir;
    private Double bbr;
    private Double initialMargin;
    private Double poundPaidPerBorrowed;
    private Double monthlyPaymentStressed;
    private String decisionAml;
    private String decisionFraud;
    private Double baseRateStressed;
    private Double aprcStressed;
    private Double propertyValuation;
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
}
