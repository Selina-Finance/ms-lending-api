package com.selina.lending.api.support.validator;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicantDto;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MaximumOnePrimaryApplicantImplTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldReturnValidWhenOnlyOnePrimaryApplicant() {
        //Given
        var request = buildQuickQuoteApplicationRequest(List.of(QuickQuoteApplicantDto.builder().primaryApplicant(true).build()));

        //When
        var violations = validator.validate(request);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                equalTo(false));
    }

    @Test
    void shouldReturnValidWhenPrimaryApplicantNotSet() {
        // Given
        var request = buildQuickQuoteApplicationRequest(List.of(QuickQuoteApplicantDto.builder().primaryApplicant(null).build()));

        //When
        var violations = validator.validate(request);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                equalTo(false));
    }

    @Test
    void shouldReturnValidIfOnlyOnePrimaryApplicantInList() {
        // Given
        var request = buildQuickQuoteApplicationRequest(List.of(
                QuickQuoteApplicantDto.builder().primaryApplicant(true).build(),
                QuickQuoteApplicantDto.builder().primaryApplicant(false).build()));

        //When
        var violations = validator.validate(request);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                equalTo(false));
    }

    @Test
    void shouldReturnValidIfOnlyOnePrimaryApplicantAndOnePrimaryApplicantIsNullInList() {
        // Given
        var request = buildQuickQuoteApplicationRequest(List.of(
                QuickQuoteApplicantDto.builder().primaryApplicant(true).build(),
                QuickQuoteApplicantDto.builder().primaryApplicant(null).build()));

        //When
        var violations = validator.validate(request);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                equalTo(false));
    }

    @Test
    void shouldReturnValidIfTwoPrimaryApplicantIsNullInList() {
        // Given
        var request = buildQuickQuoteApplicationRequest(List.of(
                QuickQuoteApplicantDto.builder().primaryApplicant(null).build(),
                QuickQuoteApplicantDto.builder().primaryApplicant(null).build()));

        //When
        var violations = validator.validate(request);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                equalTo(false));
    }


    @Test
    void shouldReturnInvalidWhenTwoPrimaryApplicantsInList() {
        // Given
        var request = buildQuickQuoteApplicationRequest(List.of(
                QuickQuoteApplicantDto.builder().primaryApplicant(true).build(),
                QuickQuoteApplicantDto.builder().primaryApplicant(true).build()));

        //When
        var violations = validator.validate(request);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                equalTo(true));
    }

    @Test
    void shouldReturnInvalidWhenTwoApplicantPrimaryApplicantFalseInList() {
        // Given
        var request = buildQuickQuoteApplicationRequest(List.of(
                QuickQuoteApplicantDto.builder().primaryApplicant(false).build(),
                QuickQuoteApplicantDto.builder().primaryApplicant(false).build()));

        //When
        var violations = validator.validate(request);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("must have one primary applicant")),
                equalTo(true));
    }

    private QuickQuoteApplicationRequest buildQuickQuoteApplicationRequest(List<QuickQuoteApplicantDto> applicants) {
        return QuickQuoteApplicationRequest.builder().applicants(applicants).build();
    }
}
