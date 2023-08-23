package com.selina.lending.api.dto.qq.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QuickQuoteFeesDto {
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
    @Schema(description = "should the product fees be added to the loan amount")
    private Boolean isAddProductFeesToFacility;

    private Double intermediaryFeeAmount;

    @JsonProperty("addIntermediaryFeeToLoan")
    private Boolean isAddIntermediaryFeeToLoan;

    @JsonProperty("addArrangementFeeSelinaToLoan")
    private Boolean isAddArrangementFeeSelinaToLoan;
}
