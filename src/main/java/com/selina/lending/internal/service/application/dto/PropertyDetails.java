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
public class PropertyDetails {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String postcode;
    private String buildingName;
    private String buildingNumber;
    private String subBuildingName;
    private Integer udprn;
    private String poBox;
    private String county;
    private String country;
    private Double estimatedValue;
    private String whenHasLastPurchased;
    private Double purchaseValue;
    private Integer internalFloorSpace;
    private Integer numberOfPriorCharges;
}
