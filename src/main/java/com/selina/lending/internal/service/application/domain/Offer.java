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
import lombok.Value;

@Builder
@Value
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
    Integer brokerFeesUpfront;
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
    String ercShortCode;
    Integer ercPeriodYears;
    Double maximumBalanceEsis;
    Double maxErc;
    List<Erc> ercData;
    List<RuleOutcome> ruleOutcomes;
}
