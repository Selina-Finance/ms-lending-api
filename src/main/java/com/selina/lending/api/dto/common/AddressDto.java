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

package com.selina.lending.api.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selina.lending.api.controller.SwaggerConstants;
import com.selina.lending.api.validator.AtLeastOneNotBlank;
import com.selina.lending.api.validator.Conditional;
import com.selina.lending.api.validator.EnumValue;
import com.selina.lending.api.validator.MoveOutAfterMoveInDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@NoArgsConstructor
@SuperBuilder
@Data
@Conditional(selected = "addressType", values = {"previous"}, required = {"fromDate", "toDate"})
@AtLeastOneNotBlank(fields = {"buildingName", "buildingNumber"})
@MoveOutAfterMoveInDate
public class AddressDto {
    @Schema(implementation = AddressType.class)
    @EnumValue(enumClass = AddressType.class)
    private String addressType;
    @NotBlank
    @Size(min = 3, max = 255)
    private String addressLine1;
    @Size(min = 3, max = 255)
    private String addressLine2;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z &\\-.']*$")
    @Schema(example = "London")
    @Size(min = 3, max = 255)
    private String city;

    @NotBlank
    @Size(min = 3, max = 8)
    private String postcode;
    private String buildingName;
    private String buildingNumber;
    private String subBuildingName;
    private Integer udprn;
    private String poBox;

    @Size(min = 2, max = 60)
    private String county;

    @Size(min = 2, max = 60)
    private String country;

    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
    @JsonFormat(pattern = SwaggerConstants.DATE_FORMAT)
    @PastOrPresent
    private LocalDate fromDate;

    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
    @JsonFormat(pattern = SwaggerConstants.DATE_FORMAT)
    @PastOrPresent
    private LocalDate toDate;

    enum AddressType {
        CURRENT("current"),
        PREVIOUS("previous"),
        EMPLOYER_CURRENT("employerCurrent"),
        EMPLOYER_PREVIOUS("employerPrevious"),
        WORK("work");

        private final String value;
        AddressType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
