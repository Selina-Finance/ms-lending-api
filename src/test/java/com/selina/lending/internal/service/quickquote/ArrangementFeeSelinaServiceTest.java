package com.selina.lending.internal.service.quickquote;

import com.selina.lending.internal.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.selina.lending.internal.dto.SourceType.AGGREGATOR;
import static com.selina.lending.internal.dto.SourceType.BROKER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ArrangementFeeSelinaServiceTest {

    @Mock
    TokenService tokenService;

    @InjectMocks
    ArrangementFeeSelinaService arrangementFeeSelinaService;

    @Test
    void whenTokenClaimSourceTypeIsAggregatorThenAddArrangementFeeSelinaIsTrue(){
        // Given
        when(tokenService.retrieveSourceType()).thenReturn(AGGREGATOR.toString());
        when(tokenService.retrieveArrangementFeeDiscountSelina()).thenReturn(1.0);

        // When
        var result = arrangementFeeSelinaService.getFeesFromToken();

        // Then
        assertTrue(result.getAddArrangementFeeSelina());
        assertThat(result.getArrangementFeeDiscountSelina(), equalTo(1.0));
    }

    @Test
    void whenTokenClaimSourceTypeIsBrokerThenAddArrangementFeeSelinaIsFalse(){
        // Given
        when(tokenService.retrieveSourceType()).thenReturn(BROKER.toString());
        when(tokenService.retrieveArrangementFeeDiscountSelina()).thenReturn(0.0);

        // When
        var result = arrangementFeeSelinaService.getFeesFromToken();

        // Then
        assertFalse(result.getAddArrangementFeeSelina());
        assertThat(result.getArrangementFeeDiscountSelina(), equalTo(0.0));
    }

    @Test
    void whenTokenClaimSourceTypeIsNotRecognizedThenAddArrangementFeeSelinaIsFalse(){
        // Given
        when(tokenService.retrieveSourceType()).thenReturn("some-source-type");
        when(tokenService.retrieveArrangementFeeDiscountSelina()).thenReturn(0.0);

        // When
        var result = arrangementFeeSelinaService.getFeesFromToken();

        // Then
        assertFalse(result.getAddArrangementFeeSelina());
        assertThat(result.getArrangementFeeDiscountSelina(), equalTo(0.0));
    }

    @Test
    void whenTokenClaimSourceTypeIsNullThenAddArrangementFeeSelinaAndArrangementFeeDiscountSelinaIsNull(){
        // Given
        when(tokenService.retrieveSourceType()).thenReturn(null);
        when(tokenService.retrieveArrangementFeeDiscountSelina()).thenReturn(0.0);

        // When
        var result = arrangementFeeSelinaService.getFeesFromToken();

        // Then
        assertNull(result.getAddArrangementFeeSelina());
        assertNull(result.getArrangementFeeDiscountSelina());
    }

    @Test
    void whenTokenClaimArrangementFeeDiscountSelinaIsNullThenAddArrangementFeeSelinaAndArrangementFeeDiscountSelinaIsNull(){
        // Given
        when(tokenService.retrieveSourceType()).thenReturn(AGGREGATOR.toString());
        when(tokenService.retrieveArrangementFeeDiscountSelina()).thenReturn(null);

        // When
        var result = arrangementFeeSelinaService.getFeesFromToken();

        // Then
        assertNull(result.getAddArrangementFeeSelina());
        assertNull(result.getArrangementFeeDiscountSelina());
    }

}
