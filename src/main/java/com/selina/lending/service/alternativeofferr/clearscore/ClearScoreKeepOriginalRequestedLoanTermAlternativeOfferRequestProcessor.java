package com.selina.lending.service.alternativeofferr.clearscore;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.service.alternativeofferr.AlternativeOfferRequestProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(value = ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.FEATURE_FLAG, havingValue = "true")
public class ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    public static final String FEATURE_FLAG = "features.alternativeOffers.clearscore.keepOriginalTermProcessor.enabled";

    private static final String FEATURE_TICKET = "GRO-2940";
    private static final String GROUP_A = "GroupA";
    private static final String GROUP_B = "GroupB";

    private static final String CLIENT_ID = "clearscore";

    private final ClearScoreBasicAlternativeOfferRequestProcessor clearScoreBasicAlternativeOfferRequestProcessor;

    public ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor() {
        clearScoreBasicAlternativeOfferRequestProcessor = new ClearScoreBasicAlternativeOfferRequestProcessor();
    }

    @PostConstruct
    private void init() {
        log.info("Registering ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor");
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
        return shouldApplyBasicAlternativeOfferLogic(applicants)
                ? clearScoreBasicAlternativeOfferRequestProcessor.isAlternativeRequestedLoanTerm(requestedLoanTerm, applicants)
                : isUseOriginalRequestedLoanTerm(requestedLoanTerm);
    }

    private boolean isUseOriginalRequestedLoanTerm(int requestedLoanTerm) {
        return true;
    }

    @Override
    protected int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants) {
        return shouldApplyBasicAlternativeOfferLogic(applicants)
                ? clearScoreBasicAlternativeOfferRequestProcessor.calculateAlternativeRequestedLoanTerm(requestedLoanTerm, applicants)
                : useOriginalRequestedLoanTerm(requestedLoanTerm);
    }

    private int useOriginalRequestedLoanTerm(int requestedLoanTerm) {
        return requestedLoanTerm;
    }

    private boolean shouldApplyBasicAlternativeOfferLogic(List<QuickQuoteApplicantDto> applicants) {
        try {
            var primaryApplicant = findPrimaryApplicant(applicants);
            return primaryApplicant.isPresent() && hasEvenPrimaryApplicantBirthday(primaryApplicant.get());
        } catch (Exception ex) {
            log.warn("Error while checking if basic ClearScore alternative offer logic should be applied. Applying the basic alternative logic by default!", ex);
            return true;
        }
    }

    private Optional<QuickQuoteApplicantDto> findPrimaryApplicant(List<QuickQuoteApplicantDto> applicants) {
        return applicants.stream()
                .filter(applicant -> applicant.getPrimaryApplicant() != null && applicant.getPrimaryApplicant())
                .findFirst();
    }

    private boolean hasEvenPrimaryApplicantBirthday(QuickQuoteApplicantDto primaryApplicant) {
        var birthday = LocalDate.parse(primaryApplicant.getDateOfBirth());
        return birthday.getDayOfMonth() % 2 == 0;
    }

    @Override
    protected String getTestGroupId(List<QuickQuoteApplicantDto> applicants) {
        return "%s: %s".formatted(FEATURE_TICKET, shouldApplyBasicAlternativeOfferLogic(applicants) ? GROUP_A : GROUP_B);
    }
}
