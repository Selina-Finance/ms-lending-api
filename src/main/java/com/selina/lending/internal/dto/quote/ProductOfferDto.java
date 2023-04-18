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

package com.selina.lending.internal.dto.quote;

import java.util.List;

import com.selina.lending.internal.dto.ErcDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductOfferDto {
    String id;
    String code;
    String name;
    Double requestedLoanAmount;
    Double maximumLoanAmount;
    Double offerBalance;
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
    List<ErcDto> ercData;
}
