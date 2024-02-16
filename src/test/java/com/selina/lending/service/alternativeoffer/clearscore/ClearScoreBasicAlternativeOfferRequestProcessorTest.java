package com.selina.lending.service.alternativeoffer.clearscore;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.service.alternativeofferr.clearscore.ClearScoreBasicAlternativeOfferRequestProcessor;
import com.selina.lending.service.alternativeofferr.clearscore.ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor;
import com.selina.lending.service.alternativeofferr.clearscore.ClearScoreExtendedAlternativeOfferRequestProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        ClearScoreExtendedAlternativeOfferRequestProcessor.FEATURE_FLAG + "=false",
        ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor.FEATURE_FLAG + "=false"
})
public class ClearScoreBasicAlternativeOfferRequestProcessorTest extends MapperBase {

    private static final String CLEARSCORE_CLIENT_ID = "clearscore";

    @Autowired
    private ClearScoreBasicAlternativeOfferRequestProcessor clearScoreBasicAlternativeOfferRequestProcessor;

    @Autowired(required = false)
    private ClearScoreExtendedAlternativeOfferRequestProcessor clearScoreExtendedAlternativeOfferRequestProcessor;

    @Autowired(required = false)
    private ClearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor;

    private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

    @Test
    void whenFeatureFlagsAreDisabledThenUseOnlyClearScoreBasicAlternativeOfferRequestProcessor() {
        assertThat(clearScoreBasicAlternativeOfferRequestProcessor).isNotNull();
        assertThat(clearScoreExtendedAlternativeOfferRequestProcessor).isNull();
        assertThat(clearScoreKeepOriginalRequestedLoanTermAlternativeOfferRequestProcessor).isNull();
    }

    @BeforeEach
    void setUp() {
        this.quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getApplicants().get(0).setDateOfBirth("1989-01-01");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void whenRequestedLoanTermIsLessThan3ThenLeaveOriginalRequestedLoanTerm(int originalRequestedLoanTerm) {
        quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
        clearScoreBasicAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
        assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
        assertThat(quickQuoteApplicationRequest.getTestGroupId()).isNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4})
    void whenRequestedLoanTermIsBetween3And4ThenAdjustItTo5(int originalRequestedLoanTerm) {
        quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
        clearScoreBasicAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
        assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
        assertThat(quickQuoteApplicationRequest.getTestGroupId()).isNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8, 9, 10, 15, 30})
    void whenRequestedLoanTermIsGreaterOrEqualTo5ThenLeaveOriginalValue(int originalRequestedLoanTerm) {
        quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
        clearScoreBasicAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
        assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
        assertThat(quickQuoteApplicationRequest.getTestGroupId()).isNull();
    }
}
