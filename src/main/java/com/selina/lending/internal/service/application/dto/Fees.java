package com.selina.lending.internal.service.application.dto;

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
    private boolean addAdviceFeeToLoan;
    private boolean addArrangementFeeToLoan;
    private boolean addCommissionFeeToLoan;
    private boolean addThirdPartyFeeToLoan;
    private boolean addValuationFeeToLoan;
    private Double adviceFee;
    private Double arrangementFee;
    private Double commissionFee;
    private Double thirdPartyFee;
    private Double valuationFee;
    private boolean addProductFeesToFacility;
    private Double intermediaryFeeAmount;
    private boolean addIntermediaryFeeToLoan;
}
