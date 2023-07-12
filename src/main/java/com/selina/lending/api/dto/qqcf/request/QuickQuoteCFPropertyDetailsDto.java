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

package com.selina.lending.api.dto.qqcf.request;

import com.selina.lending.api.controller.SwaggerConstants;
import com.selina.lending.api.dto.common.PriorChargesDto;
import com.selina.lending.api.dto.common.PropertyDetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class QuickQuoteCFPropertyDetailsDto extends PropertyDetailsDto {

    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE, description = "when was this property last purchased")
    private String whenLastPurchased;

    private Double purchaseValue;

    private Integer numberOfPriorCharges;

    private PriorChargesDto priorCharges;
}
