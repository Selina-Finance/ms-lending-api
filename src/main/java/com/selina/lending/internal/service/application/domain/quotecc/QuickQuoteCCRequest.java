package com.selina.lending.internal.service.application.domain.quotecc;

import com.selina.lending.internal.service.application.domain.Applicant;
import com.selina.lending.internal.service.application.domain.LoanInformation;
import com.selina.lending.internal.service.application.domain.PropertyDetails;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class QuickQuoteCCRequest {
    String externalApplicationId;
    String sourceAccount;
    List<Applicant> applicants;
    LoanInformation loanInformation;
    PropertyDetails propertyDetails;
}