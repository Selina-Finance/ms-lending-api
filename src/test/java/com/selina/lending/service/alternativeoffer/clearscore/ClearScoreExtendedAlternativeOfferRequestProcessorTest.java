package com.selina.lending.service.alternativeoffer.clearscore;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.service.alternativeofferr.clearscore.ClearScoreBasicAlternativeOfferRequestProcessor;
import com.selina.lending.service.alternativeofferr.clearscore.ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor;
import com.selina.lending.service.alternativeofferr.clearscore.ClearScoreExtendedAlternativeOfferRequestProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {ClearScoreExtendedAlternativeOfferRequestProcessor.FEATURE_FLAG + "=true"})
public class ClearScoreExtendedAlternativeOfferRequestProcessorTest extends MapperBase {

    private static final String CLEARSCORE_CLIENT_ID = "clearscore";

    @Autowired
    private ClearScoreExtendedAlternativeOfferRequestProcessor clearScoreExtendedAlternativeOfferRequestProcessor;

    @Autowired(required = false)
    private ClearScoreBasicAlternativeOfferRequestProcessor clearScoreBasicAlternativeOfferRequestProcessor;

    @Autowired(required = false)
    private ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor;

    @Test
    void whenFeatureFlagIsEnabledThenUseOnlyExtendedClearScoreAlternativeOfferRequestProcessor() {
        assertThat(clearScoreExtendedAlternativeOfferRequestProcessor).isNotNull();
        assertThat(clearScoreBasicAlternativeOfferRequestProcessor).isNull();
        assertThat(clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor).isNull();
    }

    @Nested
    class WhenGetExceptionDecidingWhatLogicToApplyThenApplyTheBasicLogicByDefault {

        private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

        @BeforeEach
        void setUp() {
            this.quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getApplicants().get(0).setDateOfBirth("1989-01-abcd");
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2})
        void whenRequestedLoanTermIsLessThan3ThenLeaveOriginalRequestedLoanTerm(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupA");
        }

        @ParameterizedTest
        @ValueSource(ints = {3, 4})
        void whenRequestedLoanTermIsBetween3And4ThenAdjustItTo5(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupA");
        }

        @ParameterizedTest
        @ValueSource(ints = {5, 6, 7, 8, 9, 10, 15, 30})
        void whenRequestedLoanTermIsGreaterOrEqualTo5ThenLeaveOriginalValue(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupA");
        }
    }

    @Nested
    class whenPrimaryApplicantBirthdayIsEvenThenApplyBasicAlternativeOfferLogic {

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
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupA");
        }

        @ParameterizedTest
        @ValueSource(ints = {3, 4})
        void whenRequestedLoanTermIsBetween3And4ThenAdjustItTo5(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupA");
        }

        @ParameterizedTest
        @ValueSource(ints = {5, 6, 7, 8, 9, 10, 15, 30})
        void whenRequestedLoanTermIsGreaterOrEqualTo5ThenLeaveOriginalValue(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupA");
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
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupB");
        }

        @ParameterizedTest
        @ValueSource(ints = {3, 4})
        void whenRequestedLoanTermIsBetween3And4ThenAdjustItTo5(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupB");
        }

        @Test
        void whenRequestedLoanTermIs5ThenAdjustItTo6() {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(5);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(6);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupB");
        }

        @Test
        void whenRequestedLoanTermIs6ThenAdjustItTo7() {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(6);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(7);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupB");
        }

        @Test
        void whenRequestedLoanTermIs7ThenAdjustItTo8() {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(7);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(8);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupB");
        }

        @Test
        void whenRequestedLoanTermIs8ThenAdjustItTo9() {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(8);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(9);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupB");
        }

        @Test
        void whenRequestedLoanTermIs9ThenAdjustItTo10() {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(9);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(10);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupB");
        }

        @ParameterizedTest
        @ValueSource(ints = {10, 11, 15, 30})
        void whenRequestedLoanTermIsGreaterOrEqualTo10ThenLeaveOriginalValue(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreExtendedAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2862: GroupB");
        }
    }
}
