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
public class PropertyDetails {

    @NotBlank(message = "addressLine1 is required")
    private String addressLine1;
    private String addressLine2;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "postCode is required")
    private String postcode;
    private String buildingName;
    private String buildingNumber;
    private String subBuildingName;
    private Integer udprn;
    private String poBox;
    private String county;
    private String country;

    @NotBlank(message = "estimatedValue is required")
    private Double estimatedValue;

    @NotBlank(message = "whenLastPurchased is required")
    private String whenLastPurchased;

    @NotBlank(message = "purchaseValue is required")
    private Double purchaseValue;
    private Integer internalFloorSpace;
    private Integer numberOfPriorCharges;
}
