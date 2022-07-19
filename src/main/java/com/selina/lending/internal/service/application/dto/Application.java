package com.selina.lending.internal.service.application.dto;

import java.util.Date;
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
public class Application {
    public String id;
    public String source;
    public String sourceClientId;
    public String sourceAccount;
    public String externalApplicationId;
    public String productCode;
    public String requestType;
    public String status;
    public String applicationStage;
    public Date statusDate;
    public Date createdDate;
    public List<Applicant> applicants;
    private LoanInformation loanInformation;
    private PropertyDetails propertyDetails;
    private List<Offer> offers;
}
