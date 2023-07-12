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

package com.selina.lending.api.dto.qq.response;

import com.selina.lending.internal.dto.ErcDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProductOfferDto {
    String id;
    String code;
    String name;
    Double requestedLoanAmount;
    Double maximumLoanAmount;
    Double offerBalance;
    Double brokerFeesIncluded;
    Double brokerFeesUpfront;
    Double totalAmountRepaid;
    Boolean hasFee;
    Double productFee;
    Boolean hasProductFeeAddedToLoan;
    Boolean isVariable;
    Integer term;
    Integer reversionTerm;
    Integer initialTerm;
    Double initialRate;
    Double initialPayment;
    Double reversionPayment;
    Double aprc;
    Boolean isAprcHeadline;
    Double ear;
    Double svr;
    String family;
    String category;
    Double ltvCap;
    String decision;
    Boolean hasErc;
    Double maxErc;
    String ercShortCode;
    Integer ercPeriodYears;
    String applyUrl;
    Double eligibility;
    Double arrangementFeeSelina;
    List<ErcDto> ercData;
}
