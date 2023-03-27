package com.selina.lending.internal.service.application.domain.quotecc.response;

import com.selina.lending.internal.service.application.domain.Offer;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class QuickQuoteCCResponse {
    String externalApplicationId;
    String status;
    List<Offer> offers; //
}