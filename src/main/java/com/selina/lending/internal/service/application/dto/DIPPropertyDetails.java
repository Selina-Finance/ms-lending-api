package com.selina.lending.internal.service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DIPPropertyDetails extends PropertyDetails {

    private String propertyType;
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

