package com.selina.lending.internal.service.application.domain.quotecc;

import com.selina.lending.internal.service.application.domain.Offer;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class QuickQuoteCFResponse {
    String externalApplicationId;
    String status;
    List<Offer> offers;
}