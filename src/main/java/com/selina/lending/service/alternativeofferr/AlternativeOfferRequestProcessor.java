package com.selina.lending.service.alternativeofferr;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class AlternativeOfferRequestProcessor {

    public void adjustAlternativeOfferRequest(String clientId, QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        if (isAlternativeOfferRequest(clientId, quickQuoteApplicationRequest)) {
            adjustAlternativeOfferRequest(quickQuoteApplicationRequest);
        }
    }

    private boolean isAlternativeOfferRequest(String clientId, QuickQuoteApplicationRequest request) {
        return isSupportedClient(clientId)
                && isSupportedPartner(request.getLead())
                && isAlternativeRequestedLoanTerm(request.getLoanInformation().getRequestedLoanTerm(), request.getApplicants());
    }

    protected abstract String getClientId();

    private boolean isSupportedClient(String clientId) {
        return getClientId().equalsIgnoreCase(clientId);
    }

    protected abstract boolean isSupportedPartner(LeadDto lead);

    protected abstract boolean isAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants);

    private void adjustAlternativeOfferRequest(QuickQuoteApplicationRequest request) {
        var requestedLoanTerm = request.getLoanInformation().getRequestedLoanTerm();

        try {
            log.info("Adjust QQ application to alternative offer [clientId={}] [externalApplicationId={}] [originalRequestedLoanTerm={}]",
                    getClientId(), request.getExternalApplicationId(), requestedLoanTerm);
            request.getLoanInformation().setRequestedLoanTerm(calculateAlternativeRequestedLoanTerm(request));
        } catch (Exception ex) {
            log.error("Error adjusting QQ application to alternative offer [clientId={}] [externalApplicationId={}] [originalRequestedLoanTerm={}]",
                    getClientId(), request.getExternalApplicationId(), requestedLoanTerm);
        }
    }

    protected abstract int calculateAlternativeRequestedLoanTerm(QuickQuoteApplicationRequest quickQuoteApplicationRequest);

}
