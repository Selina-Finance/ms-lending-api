package com.selina.lending.internal.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class DIPApplicant extends Applicant {

    @NotBlank(message = "applicantUsedAnotherName is required")
    private Boolean applicantUsedAnotherName;

    @NotBlank(message = "estimatedRetirementAge is required")
    private Integer estimatedRetirementAge;

    @NotBlank(message = "maritalStatus is required")
    private String maritalStatus;

    @NotBlank(message = "nationality is required")
    private String nationality;
    private String residentialStatus;

    @NotBlank(message = "identifier is required")
    private Integer identifier;
    private Income income;
    @NotNull(message = "employment is required")
    private Employment employment;
    private List<PreviousName> previousNames;
}
