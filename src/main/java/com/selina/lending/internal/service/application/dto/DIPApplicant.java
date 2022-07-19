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
public class DIPApplicant extends Applicant {

    private Boolean applicantUsedAnotherName;
    private Integer estimatedRetirementAge;
    private String maritalStatus;
    private String nationality;
    private String residentialStatus;
    private Integer identifier;
    private Incomes income;
    private Employment employment;
    private List<PreviousName> previousNames;
}
