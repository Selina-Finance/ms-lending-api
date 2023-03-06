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

import com.selina.lending.api.controller.SwaggerConstants;
import com.selina.lending.api.support.validator.Conditional;
import com.selina.lending.api.support.validator.EnumValue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@Conditional(selected = "ignore", values = {"true"}, required = {"reasonToIgnore"})
@Conditional(selected = "consolidate", values = {"true"}, required = {"amountToConsolidate"})
public class DetailDto {
    @NotNull
    @Schema(description = "the id of the credit commitment")
    Integer id;
    String status;
    @Schema(description = "1 for primary applicant")
    String applicant;
    String name;
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
    String dateOfBirth;
    String category;
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE, description = "the start date for the credit commitment")
    String startDate;
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE, description = "the end date for the credit commitment")
    String endDate;
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE, description = "the date the commitment was settled for a commitment where the commitment status is Settled")
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
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
    String fixedRatePeriodEndDate;
    @Schema(description = "the amount to consolidate if consolidate is true")
    Double amountToConsolidate;

    @Schema(implementation = ReasonToIgnore.class, description = "the reason to ignore the credit commitment if ignore is true")
    @EnumValue(enumClass = ReasonToIgnore.class)
    String reasonToIgnore;
    String lender;
    String type;
    Double currentBalance;
    String interestOnlyBalance;
    Double amount;
    String code;
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE)
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
