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

import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PriorChargesDto {
      Double balanceConsolidated;
      Double balanceOutstanding;
      Double monthlyPayment;
      @Pattern(regexp = LendingConstants.DATE_PATTERN, message = LendingConstants.DATE_INVALID_MESSAGE)
      @Schema(example = LendingConstants.EXAMPLE_DATE)
      String priorChargesYoungestDate;
      Double otherDebtPayments;
}
