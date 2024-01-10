package com.selina.lending.service.alternativeofferr;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExperianAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "experian";
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
    boolean isAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants) {
        return requestedLoanTerm < MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

    @Override
    int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants) {
        return MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

}
