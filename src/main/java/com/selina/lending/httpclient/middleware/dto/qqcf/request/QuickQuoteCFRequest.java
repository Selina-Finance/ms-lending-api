package com.selina.lending.httpclient.middleware.dto.qqcf.request;

import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.middleware.dto.common.LoanInformation;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class QuickQuoteCFRequest {
    private String externalApplicationId;
    private String sourceType;
    private String sourceAccount;
    private List<Applicant> applicants;
    private LoanInformation loanInformation;
    private PropertyDetails propertyDetails;
    private Fees fees;
}