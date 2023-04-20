package com.selina.lending.messaging.event.middleware;

import com.selina.lending.internal.dto.LeadDto;
import com.selina.lending.internal.service.application.domain.Applicant;
import com.selina.lending.internal.service.application.domain.LoanInformation;
import com.selina.lending.internal.service.application.domain.PropertyDetails;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MiddlewareCreateApplicationEvent {
    private String externalApplicationId;
    private String sourceAccount;
    private String source;
    private String applicationType;
    private String productCode;
    private List<Applicant> applicants;
    private LoanInformation loanInformation;
    private PropertyDetails propertyDetails;
    private LeadDto lead;
    private Boolean hasGivenConsentForMarketingCommunications;
}
