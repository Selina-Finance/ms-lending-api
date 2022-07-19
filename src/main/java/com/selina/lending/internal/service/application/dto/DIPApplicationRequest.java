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
public class DIPApplicationRequest extends ApplicationRequest {
    private String selectedOffer;
    private String selectedProduct;
    private DipLoanInformation loanInformation;
    private DIPPropertyDetails propertyDetails;
    private Fees fees;
}
