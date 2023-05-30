package com.selina.lending.internal.service.quickquote;

import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.Fees;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.selina.lending.internal.dto.SourceType.AGGREGATOR;

@Service
public class ArrangementFeeSelinaService {

    private final TokenService tokenService;

    public ArrangementFeeSelinaService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Fees getFeesFromToken() {
        var sourceType = tokenService.retrieveSourceType();
        var addArrangementFeeDiscountSelina = tokenService.retrieveArrangementFeeDiscountSelina();
        if (sourceType == null || addArrangementFeeDiscountSelina == null) {
            return Fees.builder().build();
        }
        return Fees.builder()
                .addArrangementFeeSelina(shouldAddArrangementFeeSelina(sourceType))
                .arrangementFeeDiscountSelina(getArrangementFeeDiscountSelina()).build();
    }

    private Double getArrangementFeeDiscountSelina() {
        return Optional.ofNullable(tokenService.retrieveArrangementFeeDiscountSelina())
                .orElse(1.0);
    }

    private boolean shouldAddArrangementFeeSelina(String sourceType) {
        return AGGREGATOR.toString().equals(sourceType);
    }

}
