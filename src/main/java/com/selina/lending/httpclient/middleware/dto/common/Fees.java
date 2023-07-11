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

@Builder
@Data
@Jacksonized
public class Fees {
    @JsonProperty("addAdviceFeeToLoan")
    Boolean isAddAdviceFeeToLoan;
    @JsonProperty("addArrangementFeeToLoan")
    Boolean isAddArrangementFeeToLoan;
    @JsonProperty("addCommissionFeeToLoan")
    Boolean isAddCommissionFeeToLoan;
    @JsonProperty("addThirdPartyFeeToLoan")
    Boolean isAddThirdPartyFeeToLoan;
    @JsonProperty("addValuationFeeToLoan")
    Boolean isAddValuationFeeToLoan;
    Double adviceFee;
    Double arrangementFee;
    Double commissionFee;
    Double thirdPartyFee;
    Double valuationFee;
    @JsonProperty("addProductFeesToFacility")
    Boolean isAddProductFeesToFacility;
    Double intermediaryFeeAmount;
    @JsonProperty("addIntermediaryFeeToLoan")
    Boolean isAddIntermediaryFeeToLoan;
    Boolean addArrangementFeeSelina;
    Double arrangementFeeDiscountSelina;
}
