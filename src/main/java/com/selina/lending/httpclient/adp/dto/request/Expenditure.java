package com.selina.lending.httpclient.adp.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Expenditure {
    private String expenditureType;
    private Double amountDeclared;
    private String frequency;
}
