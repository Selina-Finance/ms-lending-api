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

@SpringBootTest(properties = {ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.FEATURE_FLAG + "=true"})
public class ClearScoreKeepOriginalTermAlternativeOfferRequestProcessorTest extends MapperBase {

    private static final String CLEARSCORE_CLIENT_ID = "clearscore";

    @Autowired
    private ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor;

    @Autowired(required = false)
    private ClearScoreBasicAlternativeOfferRequestProcessor clearScoreBasicAlternativeOfferRequestProcessor;

    @Autowired(required = false)
    private ClearScoreExtendedAlternativeOfferRequestProcessor clearScoreExtendedAlternativeOfferRequestProcessor;

    @Test
    void whenFeatureFlagIsEnabledThenUseOnlyKeepOriginalRequestedLoanTermClearScoreAlternativeOfferRequestProcessor() {
        assertThat(clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor).isNotNull();
        assertThat(clearScoreBasicAlternativeOfferRequestProcessor).isNull();
        assertThat(clearScoreExtendedAlternativeOfferRequestProcessor).isNull();
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
            clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2940: GroupA");
        }

        @ParameterizedTest
        @ValueSource(ints = {3, 4})
        void whenRequestedLoanTermIsBetween3And4ThenAdjustItTo5(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2940: GroupA");
        }

        @ParameterizedTest
        @ValueSource(ints = {5, 6, 7, 8, 9, 10, 15, 30})
        void whenRequestedLoanTermIsGreaterOrEqualTo5ThenLeaveOriginalValue(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2940: GroupA");
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
            clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2940: GroupA");
        }

        @ParameterizedTest
        @ValueSource(ints = {3, 4})
        void whenRequestedLoanTermIsBetween3And4ThenAdjustItTo5(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2940: GroupA");
        }

        @ParameterizedTest
        @ValueSource(ints = {5, 6, 7, 8, 9, 10, 15, 30})
        void whenRequestedLoanTermIsGreaterOrEqualTo5ThenLeaveOriginalValue(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2940: GroupA");
        }
    }

    @Nested
    class whenPrimaryApplicantBirthdayIsOddThenApplyKeepOriginalRequestedLoanTermAlternativeOfferLogic {

        private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

        @BeforeEach
        void setUp() {
            this.quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getApplicants().get(0).setDateOfBirth("1989-01-01");
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 4, 5, 6, 7, 8, 9, 10, 15, 30})
        void shouldKeepOriginalRequestedLoanTerm(int originalRequestedLoanTerm) {
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
            clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
            assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
            assertThat(quickQuoteApplicationRequest.getTestGroupId()).isEqualTo("GRO-2940: GroupB");
        }
    }
}
