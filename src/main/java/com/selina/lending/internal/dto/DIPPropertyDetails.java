/*
 *  Copyright 2022 Selina Finance
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.selina.lending.internal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class DIPPropertyDetails extends PropertyDetails {

    @NotBlank(message = "propertyType is required")
    private String propertyType;

    @NotBlank(message = "numberOfBedrooms is required")
    private int numberOfBedrooms;
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
    private String tenure;
    private Boolean isThePropertyAnExLocalAuthority;
    private Boolean hasAGarage;
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
}

