package com.selina.lending.internal.enricher;

import com.selina.lending.api.dto.qq.response.ProductOfferDto;
import com.selina.lending.api.dto.qq.response.QuickQuoteResponse;
import com.selina.lending.internal.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationResponseEnricherTest {
    private static final String QUICK_QUOTE_BASE_URL = "https://mf-quick-quote.com";

    @Mock
    private TokenService tokenService;
    private ApplicationResponseEnricher enricher;

    @BeforeEach
    void setup() {
        enricher = new ApplicationResponseEnricher(QUICK_QUOTE_BASE_URL, tokenService);
    }


    @Test
    void enrichQuickQuoteResponseWithExternalApplicationId() {
        // given
        var id = UUID.randomUUID().toString();
        var response = QuickQuoteResponse.builder().build();

        // when
        enricher.enrichQuickQuoteResponseWithExternalApplicationId(response, id);

        // then
        assertThat(response.getExternalApplicationId(), equalTo(id));
    }

    @Test
    void enrichQuickQuoteResponseWithProductOffersApplyUrl() {
        // given
        var externalApplicationId = UUID.randomUUID().toString();
        var offerCode = "someProductCode";
        var source = "clearscore";

        when(tokenService.retrieveClientId()).thenReturn(source);

        List<ProductOfferDto> productOffersList = new ArrayList<>();
        productOffersList.add(ProductOfferDto.builder().code(offerCode).build());
        var response = QuickQuoteResponse
                .builder()
                .externalApplicationId(externalApplicationId)
                .offers(productOffersList)
                .build();

        // when
        enricher.enrichQuickQuoteResponseWithProductOffersApplyUrl(response);

        // then
        var expected = String.format("%s/aggregator?externalApplicationId=%s&productCode=%s&source=%s",
                QUICK_QUOTE_BASE_URL, externalApplicationId, offerCode, source);
        assertThat(response.getOffers().get(0).getApplyUrl(), equalTo(expected));
    }

    @Test
    void enrichQuickQuoteResponseApplyUrlDoesNotOccurIfOffersEmpty() {
        // Given
        var externalApplicationId = UUID.randomUUID().toString();
        var response = QuickQuoteResponse
                .builder()
                .externalApplicationId(externalApplicationId)
                .offers(null)
                .build();

        // When/Then
        assertDoesNotThrow(() -> enricher.enrichQuickQuoteResponseWithProductOffersApplyUrl(response));
    }
}
