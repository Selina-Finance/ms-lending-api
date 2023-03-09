/*
 * Copyright 2022 Selina Finance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.selina.lending.api.support.validator;

import com.selina.lending.internal.dto.AddressDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

class ConditionalImplTest {
    private static final String PREVIOUS_ADDRESS_TYPE = "previous";
    private static final String POSTCODE = "SW12 8AE";
    private static final String ADDRESS_LINE_1 = "address 1";
    private static final String CITY = "city";
    private static final String FROM_DATE = "2010-07-21";
    private static final String TO_DATE = "2018-09-19";
    private static final String CURRENT_ADDRESS_TYPE = "current";
    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validationFailsWhenMissingRequiredFields() {
        //Given
        var address = AddressDto.builder()
                .addressLine1(ADDRESS_LINE_1)
                .city(CITY)
                .postcode(POSTCODE)
                .addressType(PREVIOUS_ADDRESS_TYPE).build();

        //When
        var violations = validator.validate(address);

        //Then
        assertThat(violations.size(), equalTo(2));

        var violationPropertyPath =
                violations.stream().map(ConstraintViolation::getPropertyPath).map(Path::toString).collect(Collectors.toList());
        assertThat(violationPropertyPath, containsInAnyOrder("fromDate", "toDate"));
    }

    @Test
    void validationSuccessWhenRequiredFieldsPopulated() {
        //Given
        var address = AddressDto.builder()
                .addressLine1(ADDRESS_LINE_1)
                .city(CITY)
                .postcode(POSTCODE)
                .addressType(PREVIOUS_ADDRESS_TYPE)
                .fromDate(FROM_DATE)
                .toDate(TO_DATE)
                .build();

        //When
        var violations = validator.validate(address);

        //Then
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    void validationSuccessWhenValidationNotRequired() {
        //Given
        var address = AddressDto.builder()
                .addressLine1(ADDRESS_LINE_1)
                .city(CITY)
                .postcode(POSTCODE)
                .addressType(CURRENT_ADDRESS_TYPE)
                .build();

        //When
        var violations = validator.validate(address);

        //Then
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    void validationFailsWhenExceptionOccurs() {
        //Given
        var testDto = new ConditionalTestDto();

        //When
        var violations = validator.validate(testDto);

        //Then
        assertThat(violations.size(), equalTo(1));

        var violation = violations.iterator().next();
        assertThat(violation.getMessage(), equalTo("This field is required"));
    }

    @Conditional(selected = "property1", values = {"value"}, required = {"field1"})
    static class ConditionalTestDto {
    }
}