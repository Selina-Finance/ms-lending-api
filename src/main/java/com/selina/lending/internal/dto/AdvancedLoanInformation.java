package com.selina.lending.internal.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class AdvancedLoanInformation extends LoanInformation{
    @NotNull(message = "facilities is required")
    private List<Facility> facilities;
}
