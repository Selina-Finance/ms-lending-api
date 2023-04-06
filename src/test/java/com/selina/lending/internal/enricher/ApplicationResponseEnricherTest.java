package com.selina.lending.internal.enricher;

import com.selina.lending.internal.dto.quote.ProductOfferDto;
import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
public class ApplicationResponseEnricherTest {

    private ApplicationResponseEnricher enricher;

    @BeforeEach
    void setup() {
        enricher = new ApplicationResponseEnricher();
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
        ReflectionTestUtils.setField(enricher, "quickQuoteBaseUrl", "http://mf-quick-quote");
        var externalApplicationId = UUID.randomUUID().toString();
        var expected = String.format("http://mf-quick-quote?externalApplicationId=%s&offerCode=someProductCode", externalApplicationId);

        List<ProductOfferDto> productOffersList = new ArrayList<>();
        productOffersList.add(ProductOfferDto.builder().code("someProductCode").build());
        var response = QuickQuoteResponse
                .builder()
                .externalApplicationId(externalApplicationId)
                .offers(productOffersList)
                .build();

        // when
        enricher.enrichQuickQuoteResponseWithProductOffersApplyUrl(response);

        // then
        assertThat(response.getOffers().get(0).getApplyUrl(), equalTo(expected));
    }
}
