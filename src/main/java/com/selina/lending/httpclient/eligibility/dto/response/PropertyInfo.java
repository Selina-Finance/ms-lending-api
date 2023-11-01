package com.selina.lending.httpclient.eligibility.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class PropertyInfo {

    private Double estimatedValue;
}
