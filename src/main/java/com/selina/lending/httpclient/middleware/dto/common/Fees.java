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
    private Boolean isAddAdviceFeeToLoan;
    @JsonProperty("addArrangementFeeToLoan")
    private Boolean isAddArrangementFeeToLoan;
    @JsonProperty("addCommissionFeeToLoan")
    private Boolean isAddCommissionFeeToLoan;
    @JsonProperty("addThirdPartyFeeToLoan")
    private Boolean isAddThirdPartyFeeToLoan;
    @JsonProperty("addValuationFeeToLoan")
    private Boolean isAddValuationFeeToLoan;
    private Double adviceFee;
    private Double arrangementFee;
    private Double commissionFee;
    private Double thirdPartyFee;
    private Double valuationFee;
    @JsonProperty("addProductFeesToFacility")
    private Boolean isAddProductFeesToFacility;
    private Double intermediaryFeeAmount;
    @JsonProperty("addIntermediaryFeeToLoan")
    private Boolean isAddIntermediaryFeeToLoan;
    private Boolean addArrangementFeeSelina;
    @JsonProperty("addArrangementFeeSelinaToLoan")
    private Boolean isAddArrangementFeeSelinaToLoan;
    private Double arrangementFeeDiscountSelina;
}
