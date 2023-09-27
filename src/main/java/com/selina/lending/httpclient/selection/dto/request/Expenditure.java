package com.selina.lending.httpclient.selection.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Expenditure {

    String expenditureType;
    Double amountDeclared;
    String frequency;
}
