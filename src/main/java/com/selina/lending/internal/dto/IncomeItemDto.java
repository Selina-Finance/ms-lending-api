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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.selina.lending.api.controller.SwaggerConstants;
import com.selina.lending.api.support.validator.EnumValue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IncomeItemDto {
    @NotNull
    Double amount;
    @NotNull
    @Schema(implementation = IncomeType.class)
    @EnumValue(enumClass = IncomeType.class)
    String type;
    String status;
    String noIncomeSource;
    String docRequirements;
    Double amountVerified;

    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
    String incomeDate;
    String relatedYear;
    @Schema(implementation = frequency.class)
    @EnumValue(enumClass = frequency.class)
    String frequency;
    Double contractDaysWorkedWeeklyReported;
    Double contractDayRateReported;

    enum IncomeType {
        ARMED_FORCES_PENSION("Armed forces pension"),
        BEREAVEMENT_ALLOWANCE("Bereavement allowance"),
        BONUS("Bonus"),
        CAR_ALLOWANCE("Car allowance"),
        CARERS_ALLOWANCE("Carers allowance"),
        CITY_ALLOWANCE("City allowance"),
        COMMISSION("Commission"),
        CONTRACT_RATE("Contract rate"),
        DISABILITY_LIVING_ALLOWANCE("Disability living allowance - DLA"),
        DIRECTOR_GROSS_SALARY("Director gross salary"),
        DIVIDENDS("Dividends"),
        DRAWINGS("Drawings"),
        EMPLOYMENT_AND_SUPPORT_ALLOWANCE("Employment and support allowance"),
        GUARDIANS_ALLOWANCE("Guardians allowance"),
        INDUSTRY_INJURIES_DISABLEMENT_BENEFIT("Industrial injuries disablement benefit"),
        INVESTMENT_INCOME("Investment income"),
        NET_PROFIT("Net profit"),
        MAINTENANCE_INCOME("Maintenance income"),
        MOBILITY_ALLOWANCE("Mobility allowance"),
        OVERTIME("Overtime"),
        POLICE_AND_NHS_ALLOWANCE("Police and NHS allowance"),
        PRIVATE_PENSION("Private pension"),
        RENTAL_INCOME("Rental income"),
        GROSS_SALARY("Gross salary"),
        SECOND_JOB_INCOME("Second job income"),
        SHIFT_ALLOWANCE("Shift allowance"),
        STATE_PENSION("State pension"),
        WORKING_TAX_CREDIT_OR_UNIVERSAL_CREDIT_EQUIVALENT("Working tax credit or Universal credit equivalent"),
        CHILD_TAX_CREDIT_OR_UNIVERSAL_CREDIT_EQUIVALENT("Child tax credit or Universal credit equivalent"),
        OTHER("Other"),
        HOUSEHOLD_GROSS_INCOME("Household Gross Income"),
        GROSS_ANNUAL_INCOME("Gross annual income");

        final String value;

        IncomeType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum frequency {
        MONTHLY("Monthly"),
        ANNUALLY("Annually");


        final String value;

        frequency(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }
}
