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

import com.selina.lending.api.dto.common.ErcDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProductOfferDto {
    private String id;
    private String code;
    private String name;
    private Double requestedLoanAmount;
    private Double maximumLoanAmount;
    private Double offerBalance;
    private Double brokerFeesIncluded;
    private Double brokerFeesUpfront;
    private Double totalAmountRepaid;
    private Boolean hasFee;
    private Double productFee;
    private Boolean hasProductFeeAddedToLoan;
    private Boolean isVariable;
    private Integer term;
    private Integer reversionTerm;
    private Integer initialTerm;
    private Double initialRate;
    private Double initialPayment;
    private Double initialMargin;
    private Double reversionPayment;
    private Double reversionMargin;
    private Double aprc;
    private Boolean isAprcHeadline;
    private Double ear;
    private Double svr;
    private String family;
    private String category;
    private Double ltvCap;
    private String decision;
    private Boolean hasErc;
    private Double netLoanAmount;
    private Double maxErc;
    private String ercShortCode;
    private Integer ercPeriodYears;
    private String applyUrl;
    private Double eligibility;
    private Double arrangementFeeSelina;
    private List<ErcDto> ercData;
}
