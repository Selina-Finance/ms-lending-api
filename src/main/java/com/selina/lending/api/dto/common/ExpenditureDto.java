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

import com.selina.lending.api.validator.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class ExpenditureDto {
    @Schema(implementation = Frequency.class)
    @EnumValue(enumClass = Frequency.class)
    String frequency;
    Integer balanceDeclared;

    @NotNull
    Double amountDeclared;

    Double paymentVerified;
    Double amountVerified;

    @NotBlank
    @Schema(implementation = ExpenditureType.class)
    @EnumValue(enumClass = ExpenditureType.class)
    String expenditureType;

    enum Frequency {
        DAILY("daily"),
        WEEKLY("weekly"),
        BI_WEEKLY("bi-weekly"),
        MONTHLY("monthly"),
        QUARTERLY("quarterly"),
        SEMI_ANNUALLY("semi-annually"),
        ANNUALLY("annually");

        final String value;

        Frequency(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    enum ExpenditureType {
        FOOD_DRINK_HOUSEKEEPING("Food, Drink, Housekeeping"),
        CLOTHING_OR_FOOTWEAR("Clothing or footwear"),
        TV_PHONE_INTERNET("TV phone and internet"),
        UTILITIES("Utilities"),
        FURNISHINGS_AND_MAINTENANCE("Furnishings and maintenance"),
        INSURANCE("Insurance"),
        COUNCIL_TAX("Council tax"),
        TRANSPORT("Transport"),
        RECREATION("Recreation"),
        GROUND_RENT("Ground rent"),
        EDUCATION_AND_CHILDCARE("Education and childcare"),
        ALIMONY("Alimony"),
        CONSUMABLES("Consumables"),
        OTHER("Other"),
        OUTGOINGS_TOTAL("Outgoings Total");

        final String value;

        ExpenditureType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
