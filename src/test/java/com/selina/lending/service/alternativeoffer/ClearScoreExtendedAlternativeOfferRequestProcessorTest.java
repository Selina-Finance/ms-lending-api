package com.selina.lending.service.alternativeoffer;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.service.alternativeofferr.ClearScoreAlternativeOfferRequestProcessor;
import com.selina.lending.service.alternativeofferr.ClearScoreExtendedAlternativeOfferRequestProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"features.alternativeOffers.clearscore.extendedProcessor.enabled=true"})
public class ClearScoreExtendedAlternativeOfferRequestProcessorTest extends MapperBase {

    private static final String CLEARSCORE_CLIENT_ID = "clearscore";

    @Autowired
    private ClearScoreExtendedAlternativeOfferRequestProcessor alternativeOfferRequestProcessor;

    @Autowired(required = false)
    private ClearScoreAlternativeOfferRequestProcessor alternativeOfferRequestProcessorOldVersion;

    @Test
    void whenFeatureFlagIsEnabledThenUseOnlyExtendedClearScoreAlternativeOfferRequestProcessor() {
        assertThat(alternativeOfferRequestProcessor).isNotNull();
        assertThat(alternativeOfferRequestProcessorOldVersion).isNull();
    }

    @Test
    void whenGetExceptionDecidingWhatLogicToApplyThenApplyTheOldLogicByDefault() {
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getApplicants().get(0).setDateOfBirth("1989-01-abcd");

        quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(5);
        alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
        assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
    }

    @Nested
    class whenPrimaryApplicantBirthdayIsEvenThenApplyOldAlternativeOfferLogic {

        private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

        @BeforeEach
        void setUp() {
            this.quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getApplicants().get(0).setDateOfBirth("1989-01-02");
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2})
        void whenRequestedLoanTermIsLessThan3ThenLeaveOriginalRequestedLoanTerm(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
        }

        @ParameterizedTest
        @ValueSource(ints = {3, 4, 5})
        void whenRequestedLoanTermIsBetween3And5ThenAdjustItTo5(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
        }

        @ParameterizedTest
        @ValueSource(ints = {6, 7, 8, 9, 10, 15, 30})
        void whenRequestedLoanTermIsGreaterThan5ThenLeaveOriginalValue(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
        }
    }

    @Nested
    class whenPrimaryApplicantBirthdayIsOddThenApplyExtendedAlternativeOfferLogic {

        private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

        @BeforeEach
        void setUp() {
            this.quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getApplicants().get(0).setDateOfBirth("1989-01-01");
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2})
        void whenRequestedLoanTermIsLessThan3ThenLeaveOriginalRequestedLoanTerm(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
        }

        @Test
        void whenRequestedLoanTermIs5ThenAdjustItTo7() {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(5);
            alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(7);
        }

        @Test
        void whenRequestedLoanTermIs6ThenAdjustItTo8() {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(6);
            alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(8);
        }

        @Test
        void whenRequestedLoanTermIs7ThenAdjustItTo9() {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(7);
            alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(9);
        }

        @ParameterizedTest
        @ValueSource(ints = {8, 9, 10})
        void whenRequestedLoanTermIsBetween8And10ThenAdjustItTo10(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(10);
        }

        @ParameterizedTest
        @ValueSource(ints = {11, 15, 30})
        void whenRequestedLoanTermIsGreaterThan10ThenLeaveOriginalValue(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            alternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
        }
    }
}
