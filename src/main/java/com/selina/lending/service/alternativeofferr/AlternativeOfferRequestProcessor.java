package com.selina.lending.service.alternativeofferr;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class AlternativeOfferRequestProcessor {

    private static final String EMPTY_STRING = "";
    private static final String TEST_GROUPS_DELIMITER = "|";

    public void adjustAlternativeOfferRequest(String clientId, QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        addTestGroupId(quickQuoteApplicationRequest);
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
            request.getLoanInformation().setRequestedLoanTerm(calculateAlternativeRequestedLoanTerm(requestedLoanTerm, request.getApplicants()));
        } catch (Exception ex) {
            log.error("Error adjusting QQ application to alternative offer [clientId={}] [externalApplicationId={}] [originalRequestedLoanTerm={}]",
                    getClientId(), request.getExternalApplicationId(), requestedLoanTerm);
        }
    }

    protected abstract int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants);

    private void addTestGroupId(QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        var testGroupId = getTestGroupId(quickQuoteApplicationRequest.getApplicants());
        if (testGroupId == null) {
            return;
        }

        var testGroupIds = quickQuoteApplicationRequest.getTestGroupId();
        testGroupIds = testGroupIds == null ? EMPTY_STRING : testGroupIds.concat(TEST_GROUPS_DELIMITER);

        quickQuoteApplicationRequest.setTestGroupId(testGroupIds.concat(testGroupId));
    }

    protected abstract String getTestGroupId(List<QuickQuoteApplicantDto> applicants);

}
