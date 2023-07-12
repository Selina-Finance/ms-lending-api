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

package com.selina.lending.api.dto.common;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FeesDto {

    @NotNull
    @JsonProperty("addAdviceFeeToLoan")
    private Boolean isAddAdviceFeeToLoan;

    @NotNull
    @JsonProperty("addArrangementFeeToLoan")
    private Boolean isAddArrangementFeeToLoan;

    @NotNull
    @JsonProperty("addCommissionFeeToLoan")
    private Boolean isAddCommissionFeeToLoan;

    @NotNull
    @JsonProperty("addThirdPartyFeeToLoan")
    private Boolean isAddThirdPartyFeeToLoan;

    @NotNull
    @JsonProperty("addValuationFeeToLoan")
    private Boolean isAddValuationFeeToLoan;

    @NotNull
    private Double adviceFee;

    @NotNull
    private Double arrangementFee;

    @NotNull
    private Double commissionFee;

    @NotNull
    private Double thirdPartyFee;

    @NotNull
    private Double valuationFee;

    @NotNull
    @JsonProperty("addProductFeesToFacility")
    @Schema(description = "should the product fees be added to the loan amount")
    private Boolean isAddProductFeesToFacility;

    private Double intermediaryFeeAmount;

    @JsonProperty("addIntermediaryFeeToLoan")
    private Boolean isAddIntermediaryFeeToLoan;
}
