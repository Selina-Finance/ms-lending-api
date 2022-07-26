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

package com.selina.lending.internal.service.application.domain.quote;

import java.util.List;

import com.selina.lending.internal.service.application.domain.Erc;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ProductOffer {
    String id;
    Double propertyValuation;
    Double requestedLoanAmount;
    Double maximumLoanAmount;
    Double offerBalance;
    Double aprc;
    Double ear;
    Double svr;
    Double productFee;
    Boolean hasFee;
    Double initialRate;
    Double initialPayment;
    Double totalAmountRepaid;
    Integer term;
    Integer initialTerm;
    Integer reversionTerm;
    Boolean hasProductFeeAddedToLoan;
    String decision;
    Double maxErc;
    Integer ercPeriodYears;
    List<Erc> ercData;
}
