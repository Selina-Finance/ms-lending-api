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

package com.selina.lending.internal.dto.creditcommitments.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.selina.lending.api.validator.Conditional;
import com.selina.lending.api.validator.EnumValue;
import com.selina.lending.internal.dto.LendingConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@Conditional(selected = "ignore", values = {"true"}, required = {"reasonToIgnore"})
@Conditional(selected = "consolidate", values = {"true"}, required = {"amountToConsolidate"})
public class DetailDto {
    @NotNull
    Integer id;
    String status;
    String applicant;
    String name;
    @Pattern(regexp = LendingConstants.DATE_PATTERN, message = LendingConstants.DATE_INVALID_MESSAGE)
    @Schema(example = LendingConstants.EXAMPLE_DATE)
    String dateOfBirth;
    String category;
    @Pattern(regexp = LendingConstants.DATE_PATTERN, message = LendingConstants.DATE_INVALID_MESSAGE)
    @Schema(example = LendingConstants.EXAMPLE_DATE)
    String startDate;
    @Pattern(regexp = LendingConstants.DATE_PATTERN, message = LendingConstants.DATE_INVALID_MESSAGE)
    @Schema(example = LendingConstants.EXAMPLE_DATE)
    String endDate;
    @Pattern(regexp = LendingConstants.DATE_PATTERN, message = LendingConstants.DATE_INVALID_MESSAGE)
    @Schema(example = LendingConstants.EXAMPLE_DATE)
    String settlementDate;
    Integer remainingTerm;
    Double outstandingBalance;
    Double monthlyPayment;
    Double creditLimit;
    Boolean securityProperty;
    Boolean adverseCredit;
    Boolean joint;
    Boolean consolidate;
    Boolean ignore;
    String repaymentType;
    String repaymentVehicle;
    String costOfRepaymentVehicle;
    String interestRateType;
    @Pattern(regexp = LendingConstants.DATE_PATTERN, message = LendingConstants.DATE_INVALID_MESSAGE)
    @Schema(example = LendingConstants.EXAMPLE_DATE)
    String fixedRatePeriodEndDate;
    Double amountToConsolidate;

    @Schema(implementation = ReasonToIgnore.class)
    @EnumValue(enumClass = ReasonToIgnore.class)
    String reasonToIgnore;
    String lender;
    String type;
    Double currentBalance;
    String interestOnlyBalance;
    Double amount;
    String code;
    @Pattern(regexp = LendingConstants.DATE_PATTERN, message = LendingConstants.DATE_INVALID_MESSAGE)
    @Schema(example = LendingConstants.EXAMPLE_DATE)
    String date;

    enum ReasonToIgnore {
        IS_SELF_FUNDING("Item is a self funding buy-to-let"),
        WILL_BE_REPAID("Item will be repaid/cleared - evidence to be provided"),
        IS_WRONGLY_ATTRIBUTED("Item is wrongly attributed"),
        IS_DISPUTED("Item is disputed - Experian to be updated"),
        TERM_LESS_SIX_MONTHS("Remaining term is less than 6 months");

        final String value;

        ReasonToIgnore(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
