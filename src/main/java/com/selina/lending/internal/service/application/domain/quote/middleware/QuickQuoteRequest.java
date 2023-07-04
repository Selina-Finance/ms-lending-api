package com.selina.lending.internal.service.application.domain.quote.middleware;

import com.selina.lending.internal.dto.LeadDto;
import com.selina.lending.internal.service.application.domain.Applicant;
import com.selina.lending.internal.service.application.domain.Fees;
import com.selina.lending.internal.service.application.domain.LoanInformation;
import com.selina.lending.internal.service.application.domain.Offer;
import com.selina.lending.internal.service.application.domain.Partner;
import com.selina.lending.internal.service.application.domain.PropertyDetails;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuickQuoteRequest {

    private String externalApplicationId;
    private String sourceAccount;
    private String source;
    private String applicationType;
    private String productCode;
    private Boolean hasGivenConsentForMarketingCommunications;
    private List<Applicant> applicants;
    private Fees fees;
    private LeadDto lead;
    private LoanInformation loanInformation;
    private PropertyDetails propertyDetails;
    private List<Offer> offers;
    private Partner partner;
}
