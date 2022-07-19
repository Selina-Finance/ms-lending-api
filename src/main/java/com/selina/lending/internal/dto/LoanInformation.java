package com.selina.lending.internal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class LoanInformation {
    @NotBlank(message = "requestedLoanAmount is required")
    private Integer requestedLoanAmount;

    @NotBlank(message = "requestedLoanTerm is required")
    private int requestedLoanTerm;

    @NotBlank(message = "numberOfApplicants is required")
    private int numberOfApplicants;

    @NotBlank(message = "loanPurpose is required")
    private String loanPurpose;
    private String desiredTimeLine;
}
