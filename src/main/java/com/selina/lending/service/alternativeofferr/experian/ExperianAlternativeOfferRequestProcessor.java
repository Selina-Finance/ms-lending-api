package com.selina.lending.service.alternativeofferr.experian;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExperianAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "experian";
    private static final int MAX_ALTERNATIVE_OFFER_LOAN_TERM = 5;

    @Override
    protected String getClientId() {
        return CLIENT_ID;
    }

    @Override
    protected boolean isSupportedPartner(LeadDto lead) {
        return true;
    }

    @Override
    protected boolean isAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants) {
        return requestedLoanTerm < MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

    @Override
    protected int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants) {
        return MAX_ALTERNATIVE_OFFER_LOAN_TERM;
    }

    @Override
    protected String getTestGroupId(List<QuickQuoteApplicantDto> applicants) {
        return null;
    }
}
