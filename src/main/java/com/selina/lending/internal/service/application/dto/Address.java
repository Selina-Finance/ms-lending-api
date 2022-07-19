package com.selina.lending.internal.service.application.dto;

import java.util.Date;

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
public class Address {
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String postcode;
    private String buildingName;
    private String buildingNumber;
    private String subBuildingName;
    private int udprn;
    private String poBox;
    private String county;
    private String country;
    private Date fromDate;
    private Date toDate;
}
