package com.selina.lending.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DIPApplicationRequest extends ApplicationRequest {
    private String selectedOffer;
    private String selectedProduct;
    private AdvancedLoanInformation loanInformation;
    private DIPPropertyDetails propertyDetails;
    private Fees fees;
}
