package com.selina.lending.service.alternativeofferr.clearscore;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class ClearScoreAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "clearscore";
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_5_YEARS = 5;
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_1_YEAR = 1;

    @PostConstruct
    private void init() {
        log.info("Registering ClearScoreAlternativeOfferRequestProcessor");
    }

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
        return requestedLoanTerm == ALTERNATIVE_OFFER_LOAN_TERM_5_YEARS;
    }

    @Override
    protected int calculateAlternativeRequestedLoanTerm(QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        return ALTERNATIVE_OFFER_LOAN_TERM_1_YEAR;
    }

}
