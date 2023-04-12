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

package com.selina.lending.internal.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.selina.lending.api.support.validator.Conditional;
import com.selina.lending.api.support.validator.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Conditional(selected = "expectsFutureIncomeDecrease", values = {"true"}, required = {"expectsFutureIncomeDecreaseReason"})
public class IncomeDto {
    @Valid
    @NotNull
    List<IncomeItemDto> income;
    Boolean doesNotHaveAnyIncome;
    Boolean expectsFutureIncomeDecrease;
    @Schema(implementation = ExpectsFutureIncomeDecreaseReasons.class, description = "the reason if expectsFutureIncomeDecrease is true")
    @EnumValue(enumClass = ExpectsFutureIncomeDecreaseReasons.class)
    String expectsFutureIncomeDecreaseReason;
    Double contractDayRateVerified;
    Integer contractDaysWorkedWeeklyVerified;

    public enum ExpectsFutureIncomeDecreaseReasons {
        REDUNDANCY("Redundancy"),
        MATERNITY_OR_PATERNITY_LEAVE("Maternity or paternity leave"),
        CHNAGE_IN_EMPLOYMENT("Change in employment"),
        MOVING_TO_PART_TIME_WORK("Moving to part-time work"),
        ECONOMIC_CONDITIONS("Economic conditions"),
        OTHER("OTHER");

        final String value;

        ExpectsFutureIncomeDecreaseReasons(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
