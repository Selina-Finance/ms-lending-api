package com.selina.lending.service.alternativeofferr;

import org.springframework.stereotype.Component;

@Component
public class ExperianAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "experian";
    private static final int MAX_ALTERNATIVE_OFFER_LOAN_TERM = 5;

    @Override
    String getClientId() {
        return CLIENT_ID;
    }

    @Override
    boolean isAlternativeRequestedLoanTerm(int requestedLoanTerm) {
        return requestedLoanTerm < MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

    @Override
    int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm) {
        return MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

}
