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
public class Expenditure {
    private String frequency;
    private int balanceDeclared;
    private Double amountDeclared;
    private Double paymentVerified;
    private Double amountVerified;
    private String expenditureType;
}
