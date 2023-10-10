package com.selina.lending.service.alternativeofferr;

import com.selina.lending.api.dto.common.LeadDto;
import org.springframework.stereotype.Component;

@Component
public class MonevoAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "monevo";
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_5_YEARS = 5;
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_10_YEARS = 10;

    private static final LeadDto CONFUSED_COM_PARTNER_UTM = LeadDto.builder()
            .utmSource("aggregator")
            .utmMedium("cpc")
            .utmCampaign("_consumer_referral___confused.com_main_")
            .build();

    @Override
    String getClientId() {
        return CLIENT_ID;
    }

    @Override
    boolean isSupportedPartner(LeadDto lead) {
        return CONFUSED_COM_PARTNER_UTM.equals(lead);
    }

    @Override
    boolean isAlternativeRequestedLoanTerm(int requestedLoanTerm) {
        return requestedLoanTerm < ALTERNATIVE_OFFER_LOAN_TERM_10_YEARS;
    }

    @Override
    int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm) {
        if (requestedLoanTerm < ALTERNATIVE_OFFER_LOAN_TERM_5_YEARS) {
            return ALTERNATIVE_OFFER_LOAN_TERM_5_YEARS;
        }

        if (requestedLoanTerm <= ALTERNATIVE_OFFER_LOAN_TERM_10_YEARS) {
            return ALTERNATIVE_OFFER_LOAN_TERM_10_YEARS;
        }

        return requestedLoanTerm;
    }

}
