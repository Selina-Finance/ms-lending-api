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
    String externalApplicationId;
    String sourceAccount;
    List<Applicant> applicants;
    LoanInformation loanInformation;
    PropertyDetails propertyDetails;
    private Fees fees;
}