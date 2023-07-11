package com.selina.lending.internal.service.quickquote;

import com.selina.lending.internal.service.TokenService;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import org.springframework.stereotype.Service;

import static com.selina.lending.internal.dto.SourceType.AGGREGATOR;

@Service
public class ArrangementFeeSelinaService {

    private final TokenService tokenService;

    public ArrangementFeeSelinaService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Fees getFeesFromToken() {
        var sourceType = tokenService.retrieveSourceType();
        var arrangementFeeDiscountSelina = tokenService.retrieveArrangementFeeDiscountSelina();

        if (sourceType == null || arrangementFeeDiscountSelina == null) {
            return Fees.builder().build();
        }

        return Fees.builder()
                .addArrangementFeeSelina(shouldAddArrangementFeeSelina(sourceType))
                .arrangementFeeDiscountSelina(arrangementFeeDiscountSelina)
                .build();
    }

    private boolean shouldAddArrangementFeeSelina(String sourceType) {
        return AGGREGATOR.toString().equals(sourceType);
    }

}
