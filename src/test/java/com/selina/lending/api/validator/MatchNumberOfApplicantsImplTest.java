package com.selina.lending.api.validator;

import com.selina.lending.api.dto.qq.request.LoanInformationDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicantDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class MatchNumberOfApplicantsImplTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @ParameterizedTest
    @CsvSource({"0,0", "1,1", "2,2"})
    void shouldReturnValidWhenApplicantsSizeAndLoanInformationNumberOfApplicantsAreEquals(int applicantsSize, int numberOfApplicants) {

        // Given
        var quickQuoteRequest = buildQuickQuoteApplicationRequest(applicantsSize, numberOfApplicants);

        var violations = validator.validate(quickQuoteRequest);

        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("should be equal to applicants size")),
                equalTo(false));
    }

    @Test
    void shouldReturnValidWhenExistsApplicantsAndLoanInformationNumberOfApplicantsIsNull() {

        // Given
        var quickQuoteRequest = buildQuickQuoteApplicationRequest(1, null);

        var violations = validator.validate(quickQuoteRequest);

        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("should be equal to applicants size")),
                equalTo(false));
    }


    @ParameterizedTest
    @CsvSource({"0,1", "0,2", "1,0", "1,2", "2,1", "2,3"})
    void shouldReturnInvalidValidWhenApplicantsSizeAndLoanInformationNumberOfApplicantsAreNotEquals(int applicantsSize, int numberOfApplicants) {

        // Given
        var quickQuoteRequest = buildQuickQuoteApplicationRequest(applicantsSize, numberOfApplicants);

        var violations = validator.validate(quickQuoteRequest);

        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("should be equal to applicants size")),
                equalTo(true));
    }

    private QuickQuoteApplicationRequest buildQuickQuoteApplicationRequest(int applicantsSize, Integer numberOfApplicants) {
        var loanInformation = LoanInformationDto.builder().numberOfApplicants(numberOfApplicants).build();
        var quickQuoteApplicationRequestBuilder = QuickQuoteApplicationRequest
                .builder()
                .loanInformation(loanInformation);

        List<QuickQuoteApplicantDto> applicants = IntStream.range(0, applicantsSize)
                .mapToObj(index -> QuickQuoteApplicantDto.builder().build()).collect(Collectors.toList());

        return quickQuoteApplicationRequestBuilder.applicants(applicants).build();
    }

}
