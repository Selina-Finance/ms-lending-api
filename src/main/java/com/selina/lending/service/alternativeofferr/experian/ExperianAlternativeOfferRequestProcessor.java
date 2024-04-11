package com.selina.lending.service.alternativeofferr.experian;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import com.selina.lending.util.ABTestUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExperianAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "experian";
    private static final int LOAN_TERM_5_YEARS = 5;
    private static final int LOAN_TERM_1_YEAR = 1;
    private static final String TEST_GROUP_ID_GRO_2936_FORMAT = "GRO-3053: %s";

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
        return requestedLoanTerm >= LOAN_TERM_5_YEARS;
    }

    @Override
    protected int calculateAlternativeRequestedLoanTerm(QuickQuoteApplicationRequest request) {
        var requestedLoanTerm = request.getLoanInformation().getRequestedLoanTerm();

        if (ABTestUtils.hasOddPrimaryApplicantBirthday(request.getApplicants())) {
            ABTestUtils.appendTestGroupId(request, TEST_GROUP_ID_GRO_2936_FORMAT.formatted(ABTestUtils.GROUP_B));

            if (requestedLoanTerm == LOAN_TERM_5_YEARS) {
                requestedLoanTerm = LOAN_TERM_1_YEAR;
            }
        } else {
            ABTestUtils.appendTestGroupId(request, TEST_GROUP_ID_GRO_2936_FORMAT.formatted(ABTestUtils.GROUP_A));
        }


        return requestedLoanTerm;
    }

}
