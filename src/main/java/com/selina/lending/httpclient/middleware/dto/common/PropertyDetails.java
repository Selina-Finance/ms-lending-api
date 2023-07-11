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
    String addressLine1;
    String addressLine2;
    String city;
    String postcode;
    String buildingName;
    String buildingNumber;
    String subBuildingName;
    String propertyName;
    Integer udprn;
    String poBox;
    String county;
    String country;
    Double estimatedValue;
    String whenHasLastPurchased;
    Double purchaseValue;
    Integer propertyInternalFloorSpace;
    Integer numberOfPriorCharges;
    String propertyType;
    Integer numberOfBedrooms;
    Integer numberOfBathrooms;
    Boolean hasBeenAffectedByJapaneseKnotWeed;
    Boolean hasBeenBuiltInThePast2Years;
    Boolean hasSection20NoticeServed;
    Boolean hasSharedOwnership;
    Boolean hasSufferedFromSubsidence;
    Boolean is4thFloorOrHigherWithoutLift;
    Boolean isHmoOrMub;
    Boolean isListedAsGradeIOrGradeIiOrA;
    Boolean notOfStandardConstruction;
    String constructionYear;
    String tenure;
    Boolean isThePropertyAnExLocalAuthority;
    Boolean hasAGarage;
    Integer numberOfParkingSpaces;
    Integer yearsRemainingOnLease;
    Integer hometrackConfidenceLevel;
    Double hometrackValuation;
    Double ricsValuation;
    String buildingWarrantyCertificate;
    Boolean isAboveCommercialProperty;
    String typeOfCommercialPremises;
    Integer floorsInBuilding;
    String floorOfFlat;
    Boolean notResidentialUse;
    Boolean isApplicantResidence;
    Boolean isAboveCommercialPropertyDetails;
    String areaOfOutdoorSpace;
    String finishQuality;
    Double valuationVerified;
    Double propertyRiskValuation;
    String propertyRiskValuationComment;
    String finalValuationChannel;
    String googleMaps;
    PriorCharges priorCharges;
  }
