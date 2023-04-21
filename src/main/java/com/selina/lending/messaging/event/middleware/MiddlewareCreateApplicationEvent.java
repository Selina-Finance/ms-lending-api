package com.selina.lending.messaging.event.middleware;

import com.selina.lending.internal.dto.LeadDto;
import com.selina.lending.internal.service.application.domain.Applicant;
import com.selina.lending.internal.service.application.domain.Fees;
import com.selina.lending.internal.service.application.domain.LoanInformation;
import com.selina.lending.internal.service.application.domain.PropertyDetails;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class MiddlewareCreateApplicationEvent {
    private String externalApplicationId;
    private String sourceAccount;
    private String source;
    private String applicationType;
    private String productCode;
    private List<Applicant> applicants;
    private Fees fees;
    private LeadDto lead;
    private LoanInformation loanInformation;
    private PropertyDetails propertyDetails;
    private Boolean hasGivenConsentForMarketingCommunications;
}
