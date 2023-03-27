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

package com.selina.lending.internal.service.application.domain.quotecc.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class Offer {
    Boolean active;
    Double affordabilityDeficit;
    Double aprc;
    Double brokerFeesIncluded;
    Integer brokerFeesUpfront;
    String category;
    Checklist checklist;
    Double cltv;
    String decision;
    String eSignUrl;
    List<Erc> ercData;
    Integer ercPeriodYears;
    String ercProfile;
    String ercShortCode;
    Integer externalSettlement;
    String family;
    Boolean hasFee;
    String id;
    Double initialPayment;
    Double initialRate;
    Integer initialTerm;
    Boolean isVariable;
    Double lti;
    Double ltvCap;
    Double maxErc;
    Integer maximumAdvance;
    Integer maximumAdvancePrime;
    Double maximumBalanceEsis;
    Double monthlyAffordabilityStatus;
    Double offerBalance;
    String plan;
    Double procFee;
    String product;
    String productCode;
    Double productFee;
    Boolean productFeeAddedToLoan;
    Boolean productFeeCanAdd;
    Double reversionPayment;
    Double reversionTerm;
    List<RuleOutcome> ruleOutcomes;
    Boolean selected;
    Double svr;
    String tier;
    Double totalAmountRepaid;
}