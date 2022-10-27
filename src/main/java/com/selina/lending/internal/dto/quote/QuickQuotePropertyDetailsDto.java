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

package com.selina.lending.internal.dto.quote;

import javax.validation.constraints.Pattern;

import com.selina.lending.internal.dto.LendingConstants;
import com.selina.lending.internal.dto.PriorChargesDto;
import com.selina.lending.internal.dto.PropertyDetailsDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class QuickQuotePropertyDetailsDto extends PropertyDetailsDto {

    @Pattern(regexp = LendingConstants.DATE_PATTERN, message = LendingConstants.DATE_INVALID_MESSAGE)
    private String whenLastPurchased;

    private Double purchaseValue;

    private PriorChargesDto priorCharges;
}
