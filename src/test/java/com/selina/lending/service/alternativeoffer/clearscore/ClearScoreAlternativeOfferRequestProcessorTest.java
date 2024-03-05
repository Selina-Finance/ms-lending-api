package com.selina.lending.service.alternativeoffer.clearscore;

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.service.alternativeofferr.clearscore.ClearScoreAlternativeOfferRequestProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ClearScoreAlternativeOfferRequestProcessorTest extends MapperBase {

    private static final String CLEARSCORE_CLIENT_ID = "clearscore";

    @Autowired
    private ClearScoreAlternativeOfferRequestProcessor clearScoreAlternativeOfferRequestProcessor;

    private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

    @BeforeEach
    void setUp() {
        quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getApplicants().get(0).setDateOfBirth("1989-01-01");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void whenRequestedLoanTermIsLessThan5ThenLeaveOriginalRequestedLoanTerm(int originalRequestedLoanTerm) {
        quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
        clearScoreAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
        assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
    }

    @Test
    void whenRequestedLoanTermIs5ThenMapItTo1ToMakeTheApplicationDeclined() {
        quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(5);
        clearScoreAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
        assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 15, 30})
    void whenRequestedLoanTermIsGreaterThan5ThenLeaveOriginalValue(int originalRequestedLoanTerm) {
        quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(originalRequestedLoanTerm);
        clearScoreAlternativeOfferRequestProcessor.adjustAlternativeOfferRequest(CLEARSCORE_CLIENT_ID, quickQuoteApplicationRequest);
        assertThat(quickQuoteApplicationRequest.getLoanInformation().getRequestedLoanTerm()).isEqualTo(originalRequestedLoanTerm);
    }
}
