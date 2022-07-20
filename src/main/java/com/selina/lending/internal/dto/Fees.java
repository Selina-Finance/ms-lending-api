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

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Fees {

    @NotBlank(message = "addAdviceFeeToLoan is required")
    private boolean addAdviceFeeToLoan;

    @NotBlank(message = "addArrangementFeeToLoan is required")
    private boolean addArrangementFeeToLoan;

    @NotBlank(message = "addCommissionFeeToLoan is required")
    private boolean addCommissionFeeToLoan;

    @NotBlank(message = "addThirdPartyFeeToLoan is required")
    private boolean addThirdPartyFeeToLoan;

    @NotBlank(message = "addValuationFeeToLoan is required")
    private boolean addValuationFeeToLoan;

    @NotBlank(message = "adviceFee is required")
    private Double adviceFee;

    @NotBlank(message = "arrangementFee is required")
    private Double arrangementFee;

    @NotBlank(message = "commissionFee is required")
    private Double commissionFee;

    @NotBlank(message = "thirdPartyFee is required")
    private Double thirdPartyFee;

    @NotBlank(message = "valuationFee is required")
    private Double valuationFee;

    @NotBlank(message = "addProductFeesToFacility is required")
    private boolean addProductFeesToFacility;
    private Double intermediaryFeeAmount;
    private boolean addIntermediaryFeeToLoan;
}
