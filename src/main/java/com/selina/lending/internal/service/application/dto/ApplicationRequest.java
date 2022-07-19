package com.selina.lending.internal.service.application.dto;

import java.util.List;
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
public class ApplicationRequest {

    private String requestType;
    private String source;
    private String sourceClientId;
    private String sourceAccount;
    private String productCode;
    private String reference;
    private List<Applicant> applicants;
    private List<Expenditure> expenditure;
}
