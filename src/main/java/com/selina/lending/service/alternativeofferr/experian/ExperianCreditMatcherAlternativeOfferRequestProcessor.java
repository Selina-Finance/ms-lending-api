package com.selina.lending.service.alternativeofferr.experian;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import com.selina.lending.util.ABTestUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExperianCreditMatcherAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "experian";
    private static final int LOAN_TERM_5_YEARS = 5;
    private static final int LOAN_TERM_1_YEAR = 1;

    private static final LeadDto CREDIT_MATCHER_PARTNER_UTM = LeadDto.builder()
            .utmSource("aggregator")
            .utmMedium("cpc")
            .utmCampaign("_consumer_referral___creditmatcher_main_")
            .build();

    @Override
    protected String getClientId() {
        return CLIENT_ID;
    }

    @Override
    protected boolean isSupportedPartner(LeadDto lead) {
        return CREDIT_MATCHER_PARTNER_UTM.equals(lead);
    }

    @Override
    protected boolean isAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants) {
        return requestedLoanTerm == LOAN_TERM_5_YEARS;
    }

    @Override
    protected int calculateAlternativeRequestedLoanTerm(QuickQuoteApplicationRequest request) {
        return LOAN_TERM_1_YEAR;
    }

}
