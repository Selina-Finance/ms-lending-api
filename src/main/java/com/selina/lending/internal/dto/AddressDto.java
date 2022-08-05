/*
 *  Copyright 2022 Selina Finance
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.selina.lending.internal.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.selina.lending.api.validator.EnumValueValidator;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AddressDto {
    @EnumValueValidator(enumClass = AddressType.class)
    String addressType;
    @NotBlank
    String addressLine1;
    String addressLine2;

    @NotBlank
    String city;

    @NotBlank
    String postcode;
    String buildingName;
    String buildingNumber;
    String subBuildingName;
    int udprn;
    String poBox;
    String county;
    String country;
    Date fromDate;
    Date toDate;

    enum AddressType {
        CURRENT("current"),
        PREVIOUS("previous"),
        EMPLOYER_CURRENT("employerCurrent"),
        EMPLOYER_PREVIOUS("employerPrevious"),
        WORK("work");
        final String value;
        AddressType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
