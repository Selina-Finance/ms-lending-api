package com.selina.lending.service.alternativeofferr;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(value = "features.alternativeOffers.clearscore.extendedProcessor.enabled", havingValue = "false", matchIfMissing = true)
public class ClearScoreAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "clearscore";
    private static final int MIN_ALTERNATIVE_OFFER_LOAN_TERM = 3;
    private static final int MAX_ALTERNATIVE_OFFER_LOAN_TERM = 5;

    @PostConstruct
    private void init() {
        log.info("Registering ClearScoreAlternativeOfferRequestProcessor");
    }

    @Override
    String getClientId() {
        return CLIENT_ID;
    }

    @Override
    boolean isSupportedPartner(LeadDto lead) {
        return true;
    }

    @Override
    boolean isAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants) {
        return requestedLoanTerm >= MIN_ALTERNATIVE_OFFER_LOAN_TERM
                && requestedLoanTerm < MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

    @Override
    int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants) {
        return MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

}
