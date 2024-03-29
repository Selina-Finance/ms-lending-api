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

import com.selina.lending.api.controller.SwaggerConstants;
import com.selina.lending.api.validator.LessThanOrEqualTo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Builder
@Data
@LessThanOrEqualTo(comparisonValueFieldName = "balanceConsolidated", valueToCompareToFieldName = "balanceOutstanding")
public class PriorChargesDto {

      @PositiveOrZero
      private Double balanceConsolidated;
      @PositiveOrZero
      private Double balanceOutstanding;
      @PositiveOrZero
      private Double monthlyPayment;
      @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
      @Schema(example = SwaggerConstants.EXAMPLE_DATE)
      private String priorChargesYoungestDate;
      @PositiveOrZero
      private Double otherDebtPayments;
}
