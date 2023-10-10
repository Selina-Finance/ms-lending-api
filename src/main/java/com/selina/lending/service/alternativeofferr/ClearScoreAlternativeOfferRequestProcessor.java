package com.selina.lending.service.alternativeofferr;

import com.selina.lending.api.dto.common.LeadDto;
import org.springframework.stereotype.Component;

@Component
public class ClearScoreAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "clearscore";
    private static final int MIN_ALTERNATIVE_OFFER_LOAN_TERM = 3;
    private static final int MAX_ALTERNATIVE_OFFER_LOAN_TERM = 5;

    @Override
    String getClientId() {
        return CLIENT_ID;
    }

    @Override
    boolean isSupportedPartner(LeadDto lead) {
        return true;
    }

    @Override
    boolean isAlternativeRequestedLoanTerm(int requestedLoanTerm) {
        return requestedLoanTerm >= MIN_ALTERNATIVE_OFFER_LOAN_TERM
                && requestedLoanTerm < MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

    @Override
    int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm) {
        return MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

}
