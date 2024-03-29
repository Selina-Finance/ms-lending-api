package com.selina.lending.httpclient.middleware.dto.qqcf.response;

import com.selina.lending.httpclient.middleware.dto.common.Offer;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class QuickQuoteCFResponse {
    private String externalApplicationId;
    private String status;
    private List<Offer> offers;
}