package com.selina.lending.service.alternativeofferr;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AlternativeOfferRequestProcessor {

    public void adjustAlternativeOfferRequest(String s, QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        if (isAlternativeOfferRequest(s, quickQuoteApplicationRequest)) {
            adjustAlternativeOfferRequest(quickQuoteApplicationRequest);
        }
    }

    private boolean isAlternativeOfferRequest(String clientId, QuickQuoteApplicationRequest request) {
        return getClientId().equalsIgnoreCase(clientId)
                && isAlternativeRequestedLoanTerm(request.getLoanInformation().getRequestedLoanTerm());
    }

    abstract String getClientId();

    abstract boolean isAlternativeRequestedLoanTerm(int requestedLoanTerm);

    private void adjustAlternativeOfferRequest(QuickQuoteApplicationRequest request) {
        var requestedLoanTerm = request.getLoanInformation().getRequestedLoanTerm();

        try {
            log.info("Adjust QQ application to alternative offer [clientId={}] [externalApplicationId={}] [originalRequestedLoanTerm={}]",
                    getClientId(), request.getExternalApplicationId(), requestedLoanTerm);
            request.getLoanInformation().setRequestedLoanTerm(calculateAlternativeRequestedLoanTerm(requestedLoanTerm));
        } catch (Exception ex) {
            log.error("Error adjusting QQ application to alternative offer [clientId={}] [externalApplicationId={}] [originalRequestedLoanTerm={}]",
                    getClientId(), request.getExternalApplicationId(), requestedLoanTerm);
        }
    }

    abstract int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm);
}
