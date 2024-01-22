package com.selina.lending.service.alternativeofferr;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(value = "features.alternativeOffers.clearscore.extendedProcessor.enabled", havingValue = "true")
public class ClearScoreExtendedAlternativeOfferRequestProcessor extends AlternativeOfferRequestProcessor {

    private static final String CLIENT_ID = "clearscore";
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_3_YEARS = 3;
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_5_YEARS = 5;
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_6_YEARS = 6;
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_7_YEARS = 7;
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_8_YEARS = 8;
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_9_YEARS = 9;
    private static final int ALTERNATIVE_OFFER_LOAN_TERM_10_YEARS = 10;

    private final ClearScoreAlternativeOfferRequestProcessor clearScoreAlternativeOfferRequestProcessor;

    public ClearScoreExtendedAlternativeOfferRequestProcessor() {
        clearScoreAlternativeOfferRequestProcessor = new ClearScoreAlternativeOfferRequestProcessor();
    }

    @PostConstruct
    private void init() {
        log.info("Registering ClearScoreExtendedAlternativeOfferRequestProcessor");
    }

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
        return shouldApplyOldAlternativeOfferLogic(applicants)
                ? clearScoreAlternativeOfferRequestProcessor.isAlternativeRequestedLoanTerm(requestedLoanTerm, applicants)
                : isExtendedAlternativeRequestedLoanTerm(requestedLoanTerm);
    }

    private boolean isExtendedAlternativeRequestedLoanTerm(int requestedLoanTerm) {
        return requestedLoanTerm >= ALTERNATIVE_OFFER_LOAN_TERM_3_YEARS
                && requestedLoanTerm < ALTERNATIVE_OFFER_LOAN_TERM_10_YEARS;
    }

    @Override
    int calculateAlternativeRequestedLoanTerm(int requestedLoanTerm, List<QuickQuoteApplicantDto> applicants) {
        return shouldApplyOldAlternativeOfferLogic(applicants)
                ? clearScoreAlternativeOfferRequestProcessor.calculateAlternativeRequestedLoanTerm(requestedLoanTerm, applicants)
                : calculateExtendedAlternativeRequestedLoanTerm(requestedLoanTerm);
    }

    private int calculateExtendedAlternativeRequestedLoanTerm(int requestedLoanTerm) {
        if (requestedLoanTerm >= ALTERNATIVE_OFFER_LOAN_TERM_3_YEARS
                && requestedLoanTerm < ALTERNATIVE_OFFER_LOAN_TERM_5_YEARS) {
            return ALTERNATIVE_OFFER_LOAN_TERM_5_YEARS;
        }

        if (requestedLoanTerm == ALTERNATIVE_OFFER_LOAN_TERM_5_YEARS) {
            return ALTERNATIVE_OFFER_LOAN_TERM_6_YEARS;
        }

        if (requestedLoanTerm == ALTERNATIVE_OFFER_LOAN_TERM_6_YEARS) {
            return ALTERNATIVE_OFFER_LOAN_TERM_7_YEARS;
        }

        if (requestedLoanTerm == ALTERNATIVE_OFFER_LOAN_TERM_7_YEARS) {
            return ALTERNATIVE_OFFER_LOAN_TERM_8_YEARS;
        }

        if (requestedLoanTerm == ALTERNATIVE_OFFER_LOAN_TERM_8_YEARS) {
            return ALTERNATIVE_OFFER_LOAN_TERM_9_YEARS;
        }

        if (requestedLoanTerm == ALTERNATIVE_OFFER_LOAN_TERM_9_YEARS) {
            return ALTERNATIVE_OFFER_LOAN_TERM_10_YEARS;
        }

        return requestedLoanTerm;
    }

    private boolean shouldApplyOldAlternativeOfferLogic(List<QuickQuoteApplicantDto> applicants) {
        try {
            var primaryApplicant = findPrimaryApplicant(applicants);
            return primaryApplicant.isPresent() && hasEvenPrimaryApplicantBirthday(primaryApplicant.get());
        } catch (Exception ex) {
            log.warn("Error while checking if old ClearScore alternative offer logic should be applied. Applying the old logic by default!", ex);
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
}
