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

package com.selina.lending.httpclient.middleware.dto.common;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class PropertyDetails {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String postcode;
    private String buildingName;
    private String buildingNumber;
    private String subBuildingName;
    private String propertyName;
    private Integer udprn;
    private String poBox;
    private String county;
    private String country;
    private Double estimatedValue;
    private String whenHasLastPurchased;
    private Double purchaseValue;
    private Integer propertyInternalFloorSpace;
    private Integer numberOfPriorCharges;
    private String propertyType;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
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
    private Integer numberOfParkingSpaces;
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
    private String areaOfOutdoorSpace;
    private String finishQuality;
    private Double valuationVerified;
    private Double propertyRiskValuation;
    private String propertyRiskValuationComment;
    private String finalValuationChannel;
    private String googleMaps;
    private PriorCharges priorCharges;
  }
