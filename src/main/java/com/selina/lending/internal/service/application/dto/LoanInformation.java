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
public class LoanInformation {
    private Integer requestedLoanAmount;
    private int requestedLoanTerm;
    private int numberOfApplicants;
    private String loanPurpose;
    private String desiredTimeLine;
}
