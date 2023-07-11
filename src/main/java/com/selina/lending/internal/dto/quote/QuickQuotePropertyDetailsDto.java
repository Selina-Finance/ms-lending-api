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

import com.selina.lending.api.controller.swagger.SwaggerConstants;
import com.selina.lending.api.support.validator.AtLeastOneNotBlank;
import com.selina.lending.internal.dto.PriorChargesDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode
@AtLeastOneNotBlank(fields = {"buildingName", "buildingNumber"})
public class QuickQuotePropertyDetailsDto {

    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE, description = "when was this property last purchased")
    private String whenLastPurchased;

    private Double purchaseValue;

    private Integer numberOfPriorCharges;

    private PriorChargesDto priorCharges;

    @NotBlank
    @Size(min = 3, max = 255)
    private String addressLine1;
    @Size(min = 3, max = 255)
    private String addressLine2;

    @NotBlank
    @Size(min = 3, max = 255)
    private String city;

    @NotBlank
    @Size(min = 3, max = 8)
    private String postcode;
    private String buildingName;
    private String buildingNumber;
    private String subBuildingName;
    private String propertyName;
    private Integer udprn;
    private String poBox;

    @Size(min = 2, max = 60)
    private String county;

    @Size(min = 2, max = 60)
    private String country;

    @Range(min = 50_000, max = 99_999_999)
    private Double estimatedValue;

    private Integer propertyInternalFloorSpace;
}
