package com.selina.lending.api.support.validator;

import com.selina.lending.internal.dto.AddressDto;
import com.selina.lending.internal.mapper.MapperBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class MoveOutAfterMoveInDateImplTest extends MapperBase {

    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void whenAddressTypeIsNullThenReturnValid() {
        // Given
        var addressDto = getAddressDto();
        addressDto.setAddressType(null);

        // When
        var violations = validator.validate(addressDto);

        // Then
        assertThat(violations, hasSize(0));
    }

    @Test
    void shouldReturnValidWhenFromDateIsNull() {
        // Given
        var toDate = LocalDate.parse("2000-01-01");

        var addressDto = getAddressDto(null, toDate);

        //When
        var violations = validator.validate(addressDto);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("toDate must exceed fromDate")), equalTo(false));
    }

    @Test
    void shouldReturnValidWhenToDateIsNull() {
        // Given
        var fromDate = LocalDate.parse("2000-01-01");

        var addressDto = getAddressDto(fromDate, null);

        //When
        var violations = validator.validate(addressDto);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("toDate must exceed fromDate")), equalTo(false));

    }

    @Test
    void shouldReturnInvalidWhenToDateBeforeFromDate() {
        // Given
        var fromDate = LocalDate.parse("2000-01-01");
        var toDate = LocalDate.parse("1970-01-01");

        var addressDto =  getAddressDto(fromDate, toDate);

        //When
        var violations = validator.validate(addressDto);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("toDate must exceed fromDate")), equalTo(true));
    }

    @Test
    void shouldReturnValidWhenToDateAfterFromDate() {
        // Given
        var fromDate = LocalDate.parse("1970-01-01");
        var toDate = LocalDate.parse("2000-01-01");

        var addressDto = getAddressDto(fromDate, toDate);

        //When
        var violations = validator.validate(addressDto);

        //Then
        assertThat(violations.stream().anyMatch(m -> m.getMessage().equals("toDate must exceed fromDate")), equalTo(false));
    }

    private AddressDto getAddressDto(LocalDate fromDate, LocalDate toDate) {
        return AddressDto.builder()
                .addressLine1("")
                .addressType("previous")
                .buildingNumber("")
                .city("")
                .postcode("")
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
    }
}
