package com.selina.lending.service.alternativeofferr.clearscore;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(value = {
        ClearScoreExtendedAlternativeOfferRequestProcessor.FEATURE_FLAG,
        ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.FEATURE_FLAG
}, havingValue = "false", matchIfMissing = true)
public class ClearScoreBasicAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "clearscore";
    private static final int MIN_ALTERNATIVE_OFFER_LOAN_TERM = 3;
    private static final int MAX_ALTERNATIVE_OFFER_LOAN_TERM = 5;

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
        return requestedLoanTerm >= MIN_ALTERNATIVE_OFFER_LOAN_TERM
                && requestedLoanTerm < MAX_ALTERNATIVE_OFFER_LOAN_TERM;
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
