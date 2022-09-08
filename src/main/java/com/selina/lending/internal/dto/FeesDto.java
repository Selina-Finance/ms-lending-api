/*
 *  Copyright 2022 Selina Finance
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.selina.lending.internal.dto;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FeesDto {

    @NotNull
    Boolean addAdviceFeeToLoan;

    @NotNull
    Boolean addArrangementFeeToLoan;

    @NotNull
    Boolean addCommissionFeeToLoan;

    @NotNull
    Boolean addThirdPartyFeeToLoan;

    @NotNull
    Boolean addValuationFeeToLoan;

    @NotNull
    Double adviceFee;

    @NotNull
    Double arrangementFee;

    @NotNull
    Double commissionFee;

    @NotNull
    Double thirdPartyFee;

    @NotNull
    Double valuationFee;

    @NotNull
    Boolean addProductFeesToFacility;
    Double intermediaryFeeAmount;
    Boolean addIntermediaryFeeToLoan;
}
