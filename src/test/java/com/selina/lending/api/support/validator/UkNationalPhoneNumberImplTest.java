package com.selina.lending.api.support.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


class UkNationalPhoneNumberImplTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"01234567890", "07777777777","+441234567890", "+44 123 456 789 0"})
    void shouldBeValidPhoneNumber(String phoneNumber) {

        // Given
        var request = new UkNationalPhoneTestClass(phoneNumber);

        // When
        var violations = validator.validate(request);

        // Then
        assertThat(violations.size(), equalTo(0));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {
            "+7(123)123123", "123", "123456", "0123456789", "12345678901", "012345AB90", "+4401111111111", "012 34554 90", "012345678901",
            "07433w121789", "07433w121789", "+351910000000", "+7(123)123123"
    })
    void shouldReturnInvalidWhenIsNotValidPhoneNumber(String phoneNumber) {

        // Given
        var request = new UkNationalPhoneTestClass(phoneNumber);

        // When
        var violations = validator.validate(request);

        // Then
        assertThat(violations.size(), equalTo(1));
        assertThat(violations.iterator().next().getMessage(), equalTo("must be a valid UK phone number"));
    }


    record UkNationalPhoneTestClass(@UkNationalPhoneNumber String phoneNumber) { }

}
