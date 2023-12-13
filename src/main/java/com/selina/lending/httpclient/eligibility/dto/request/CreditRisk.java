package com.selina.lending.httpclient.eligibility.dto.request;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreditRisk {

    String conductStatus;
    Double ltv;
    Double lti;
    Double dti;
}
