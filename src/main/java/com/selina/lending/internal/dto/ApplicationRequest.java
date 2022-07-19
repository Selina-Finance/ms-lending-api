package com.selina.lending.internal.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ApplicationRequest {

    private String requestType;

    @NotBlank(message = "source is required")
    private String source;
    private String sourceClientId;
    private String sourceAccount;

    @NotBlank(message = "productCode is required")
    private String productCode;
    private String reference;

    @Size(message = "applicants is required", min = 1, max = 2)
    private List<Applicant> applicants;
    private List<Expenditure> expenditure;
}
