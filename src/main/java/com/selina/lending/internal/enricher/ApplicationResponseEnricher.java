package com.selina.lending.internal.enricher;

import com.selina.lending.internal.dto.quote.ProductOfferDto;
import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplicationResponseEnricher {

    private final String quickQuoteBaseUrl;

    public ApplicationResponseEnricher(@Value(value = "${quickquote.web.url}") String quickQuoteBaseUrl) {
        this.quickQuoteBaseUrl = quickQuoteBaseUrl;
    }
    public void enrichQuickQuoteResponseWithExternalApplicationId(QuickQuoteResponse response, String externalApplicationId) {
        response.setExternalApplicationId(externalApplicationId);
    }

    public void enrichQuickQuoteResponseWithProductOffersApplyUrl(QuickQuoteResponse response) {
        var offers = response.getOffers();
        if (offers != null) {
            for (ProductOfferDto offer : offers) {
                offer.setApplyUrl(
                        this.quickQuoteProductOffersApplyUrlBuilder(response.getExternalApplicationId(), offer.getCode())
                );
            }
        }
    }

    private String quickQuoteProductOffersApplyUrlBuilder(String externalApplicationId, String productCode){
        return String.format("%s?externalApplicationId=%s&offerCode=%s", this.quickQuoteBaseUrl, externalApplicationId, productCode);
    }

}
