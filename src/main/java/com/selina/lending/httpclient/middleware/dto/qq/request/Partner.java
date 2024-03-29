package com.selina.lending.httpclient.middleware.dto.qq.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Partner {
    private String companyId;
    private String subUnitId;
}
