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

package com.selina.lending.api.dto.dip.request;

import com.selina.lending.api.controller.SwaggerConstants;
import com.selina.lending.api.dto.common.PriorChargesDto;
import com.selina.lending.api.dto.common.PropertyDetailsDto;
import com.selina.lending.api.validator.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class DIPPropertyDetailsDto extends PropertyDetailsDto {

    @NotBlank
    @Schema(implementation = PropertyType.class, description = "the property type")
    @EnumValue(enumClass = PropertyType.class)
    private String propertyType;

    @NotBlank
    @Pattern(regexp = SwaggerConstants.DATE_PATTERN, message = SwaggerConstants.DATE_INVALID_MESSAGE)
    @Schema(example = SwaggerConstants.EXAMPLE_DATE, description = "when was this property last purchased")
    private String whenLastPurchased;

    @NotNull
    private Double purchaseValue;

    @NotNull
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private Integer numberOfParkingSpaces;
    private Boolean hasBeenAffectedByJapaneseKnotWeed;
    private Boolean hasBeenBuiltInThePast2Years;
    private Boolean hasSection20NoticeServed;
    private Boolean hasSharedOwnership;
    private Boolean hasSufferedFromSubsidence;
    private Boolean is4thFloorOrHigherWithoutLift;
    private Boolean isHmoOrMub;
    private Boolean isListedAsGradeIOrGradeIiOrA;
    private Boolean notOfStandardConstruction;
    private String constructionYear;
    @Schema(implementation = Tenure.class)
    @EnumValue(enumClass = Tenure.class)
    private String tenure;
    private Boolean isThePropertyAnExLocalAuthority;
    private Integer yearsRemainingOnLease;
    private Integer hometrackConfidenceLevel;
    private Double hometrackValuation;
    private Double ricsValuation;
    private String buildingWarrantyCertificate;
    private Boolean isAboveCommercialProperty;
    private String typeOfCommercialPremises;
    private Integer floorsInBuilding;
    private String floorOfFlat;
    private Boolean notResidentialUse;
    private Boolean isApplicantResidence;
    private Boolean isAboveCommercialPropertyDetails;
    private Integer numberOfPriorCharges;

    @Valid
    private PriorChargesDto priorCharges;
}

