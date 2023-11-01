package com.selina.lending.httpclient.eligibility.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PropertyInfo {

    private Double estimatedValue;
}
