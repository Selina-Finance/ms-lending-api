/*
 *   Copyright 2022 Selina Finance
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package com.selina.lending.internal.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class PropertyDetailsMapperTest extends MapperBase {

    @Test
    void mapToPropertyDetailsDto() {
        //Given
        var propertyDetails = getPropertyDetails();

        //When
        var propertyDetailsDto = PropertyDetailsMapper.INSTANCE.mapToPropertyDetailsDto(propertyDetails);

        //Then
        assertThat(propertyDetailsDto.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(propertyDetailsDto.getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(propertyDetailsDto.getPurchaseValue(), equalTo(PURCHASE_VALUE));
        assertThat(propertyDetailsDto.getCountry(), equalTo(COUNTRY));
        assertThat(propertyDetailsDto.getBuildingNumber(), equalTo(BUILDING_NUMBER));
        assertThat(propertyDetailsDto.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(propertyDetailsDto.getCity(), equalTo(CITY));
        assertThat(propertyDetailsDto.getPostcode(), equalTo(POSTCODE));
        assertThat(propertyDetailsDto.getCounty(), equalTo(COUNTY));
        assertThat(propertyDetailsDto.getWhenLastPurchased(), equalTo(WHEN_LAST_PURCHASED));
    }
    @Test
    public void mapToDIPPropertyDetailsDto() {
        //Given
        var propertyDetails = getPropertyDetails();

        //When
        var dipPropertyDetailsDto = PropertyDetailsMapper.INSTANCE.mapToPropertyDetailsDto(propertyDetails);

        //Then
        assertThat(dipPropertyDetailsDto.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(dipPropertyDetailsDto.getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(dipPropertyDetailsDto.getPurchaseValue(), equalTo(PURCHASE_VALUE));
        assertThat(dipPropertyDetailsDto.getCountry(), equalTo(COUNTRY));
        assertThat(dipPropertyDetailsDto.getBuildingNumber(), equalTo(BUILDING_NUMBER));
        assertThat(dipPropertyDetailsDto.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(dipPropertyDetailsDto.getCity(), equalTo(CITY));
        assertThat(dipPropertyDetailsDto.getPostcode(), equalTo(POSTCODE));
        assertThat(dipPropertyDetailsDto.getCounty(), equalTo(COUNTY));
        assertThat(dipPropertyDetailsDto.getWhenLastPurchased(), equalTo(WHEN_LAST_PURCHASED));
        assertThat(dipPropertyDetailsDto.getPropertyType(), equalTo(PROPERTY_TYPE));
        assertThat(dipPropertyDetailsDto.getNumberOfBedrooms(), equalTo(NUMBER_OF_BEDROOMS));
    }

    @Test
    void mapToPropertyDetailsFromPropertyDetailsDto() {
        //Given
        var propertyDetailsDto = getPropertyDetailsDto();

        //When
        var propertyDetails = PropertyDetailsMapper.INSTANCE.mapToPropertyDetails(propertyDetailsDto);

        //Then
        assertThat(propertyDetails.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(propertyDetails.getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(propertyDetails.getPurchaseValue(), equalTo(PURCHASE_VALUE));
        assertThat(propertyDetails.getCountry(), equalTo(COUNTRY));
        assertThat(propertyDetails.getBuildingNumber(), equalTo(BUILDING_NUMBER));
        assertThat(propertyDetails.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(propertyDetails.getCity(), equalTo(CITY));
        assertThat(propertyDetails.getPostcode(), equalTo(POSTCODE));
        assertThat(propertyDetails.getCounty(), equalTo(COUNTY));
        assertNull(propertyDetails.getHasAGarage());
    }

    @Test
    void mapToPropertyDetailsFromDIPPropertyDetailsDto() {
        //Given
        var dipPropertyDetailsDto = getDIPPropertyDetailsDto();

        //When
        var propertyDetails = PropertyDetailsMapper.INSTANCE.mapToPropertyDetails(dipPropertyDetailsDto);

        //Then
        assertThat(propertyDetails.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(propertyDetails.getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(propertyDetails.getPurchaseValue(), equalTo(PURCHASE_VALUE));
        assertThat(propertyDetails.getCountry(), equalTo(COUNTRY));
        assertThat(propertyDetails.getBuildingNumber(), equalTo(BUILDING_NUMBER));
        assertThat(propertyDetails.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(propertyDetails.getCity(), equalTo(CITY));
        assertThat(propertyDetails.getPostcode(), equalTo(POSTCODE));
        assertThat(propertyDetails.getCounty(), equalTo(COUNTY));
        assertThat(propertyDetails.getPropertyType(), equalTo(PROPERTY_TYPE));
        assertThat(propertyDetails.getNumberOfBedrooms(), equalTo(NUMBER_OF_BEDROOMS));
        assertThat(propertyDetails.getHasAGarage(), equalTo(true));
    }
}