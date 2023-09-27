package com.selina.lending.httpclient.middleware.dto.qq.request;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import com.selina.lending.httpclient.middleware.dto.common.Expenditure;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.middleware.dto.common.LoanInformation;
import com.selina.lending.httpclient.middleware.dto.common.Offer;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
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
    private LeadDto lead; //TODO must not reuse controller DTO
    private LoanInformation loanInformation;
    private PropertyDetails propertyDetails;
    private List<Offer> offers;
    private Partner partner;
    private List<Expenditure> expenditure;
}
