package com.selina.lending.internal.enricher;

import com.selina.lending.internal.dto.quote.ProductOfferDto;
import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplicationResponseEnricher {

    @Value(value = "${quickquote.web.url}")
    private String quickQuoteBaseUrl;

    public void enrichQuickQuoteResponseWithExternalApplicationId(QuickQuoteResponse response, String externalApplicationId) {
        response.setExternalApplicationId(externalApplicationId);
    }

    public void enrichQuickQuoteResponseWithProductOffersApplyUrl(QuickQuoteResponse response) {
        var offers = response.getOffers();
        for (ProductOfferDto offer : offers) {
            offer.setApplyUrl(
                    this.quickQuoteProductOffersApplyUrlBuilder(response.getExternalApplicationId(), offer.getCode())
            );
        }
    }

    private String quickQuoteProductOffersApplyUrlBuilder(String externalApplicationId, String productCode){
        return String.format("%s?externalApplicationId=%s&offerCode=%s", this.quickQuoteBaseUrl, externalApplicationId, productCode);
    }

}
