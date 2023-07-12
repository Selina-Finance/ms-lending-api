package com.selina.lending.internal.service.enricher;

import com.selina.lending.api.dto.qq.response.ProductOfferDto;
import com.selina.lending.api.dto.qq.response.QuickQuoteResponse;
import com.selina.lending.internal.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@Slf4j
public class ApplicationResponseEnricher {

    private final String quickQuoteBaseUrl;
    private final TokenService tokenService;

    public ApplicationResponseEnricher(@Value(value = "${quickquote.web.url}") String quickQuoteBaseUrl,
            TokenService tokenService) {
        this.quickQuoteBaseUrl = quickQuoteBaseUrl;
        this.tokenService = tokenService;
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

    private String quickQuoteProductOffersApplyUrlBuilder(String externalApplicationId, String productCode) {
        String clientId = tokenService.retrieveClientId();
        return UriComponentsBuilder.fromHttpUrl(quickQuoteBaseUrl)
                .path("/aggregator")
                .queryParamIfPresent("externalApplicationId", Optional.ofNullable(externalApplicationId))
                .queryParamIfPresent("productCode", Optional.ofNullable(productCode))
                .queryParamIfPresent("source", Optional.ofNullable(clientId))
                .encode()
                .build()
                .toString();
    }

}
