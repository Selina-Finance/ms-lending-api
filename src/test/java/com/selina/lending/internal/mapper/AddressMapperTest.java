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

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.selina.lending.internal.dto.AddressDto;
import com.selina.lending.internal.service.application.domain.Address;

public class AddressMapperTest {

    private static final String ADDRESS_LINE_1 = "address line 1";
    private static final String ADDRESS_LINE_2 = "address line 2";
    private static final String ADDRESS_TYPE = "Home";
    private static final String COUNTRY = "England";
    private static final String BUILDING_NUMBER = "10";
    private static final String CITY = "a city";
    private static final String POSTCODE = "postcode";
    private static final int UDPRN = 1235;
    private static final String PO_BOX = "poBox";
    private static final String BUILDING_NAME = "building name";
    private static final String COUNTY = "county";
    public static final Date FROM_DATE = Date.from(Instant.now());
    public static final Date TO_DATE = Date.from(Instant.now());

    @Test
    public void mapToAddress() {
        //Given
        AddressDto addressDto = AddressDto.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .addressType(ADDRESS_TYPE)
                .country(COUNTRY)
                .buildingNumber(BUILDING_NUMBER)
                .buildingName(BUILDING_NAME)
                .city(CITY)
                .postcode(POSTCODE)
                .udprn(UDPRN)
                .poBox(PO_BOX)
                .county(COUNTY)
                .fromDate(FROM_DATE)
                .toDate(TO_DATE)
                .build();

        // When
        Address address = AddressMapper.INSTANCE.mapToAddress(addressDto);

        //Then
        assertThat(ADDRESS_LINE_1, equalTo(address.getAddressLine1()));
        assertThat(ADDRESS_LINE_2, equalTo(address.getAddressLine2()));
        assertThat(ADDRESS_TYPE, equalTo(address.getAddressType()));
        assertThat(COUNTRY, equalTo(address.getCountry()));
        assertThat(BUILDING_NUMBER, equalTo(address.getBuildingNumber()));
        assertThat(BUILDING_NAME, equalTo(address.getBuildingName()));
        assertThat(CITY, equalTo(address.getCity()));
        assertThat(POSTCODE, equalTo(address.getPostcode()));
        assertThat(UDPRN, equalTo(address.getUdprn()));
        assertThat(PO_BOX, equalTo(address.getPoBox()));
        assertThat(COUNTY, equalTo(address.getCounty()));
        assertThat(FROM_DATE, equalTo(address.getFrom()));
        assertThat(TO_DATE, equalTo(address.getTo()));
    }

    @Test
    public void mapToAddressDto() {
        //Given
        Address address = Address.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .addressType(ADDRESS_TYPE)
                .country(COUNTRY)
                .buildingNumber(BUILDING_NUMBER)
                .buildingName(BUILDING_NAME)
                .city(CITY)
                .postcode(POSTCODE)
                .udprn(UDPRN)
                .poBox(PO_BOX)
                .county(COUNTY)
                .from(FROM_DATE)
                .build();

        // When
        AddressDto addressDto = AddressMapper.INSTANCE.mapToAddressDto(address);

        //Then
        assertThat(ADDRESS_LINE_1, equalTo(addressDto.getAddressLine1()));
        assertThat(ADDRESS_LINE_2, equalTo(addressDto.getAddressLine2()));
        assertThat(ADDRESS_TYPE, equalTo(addressDto.getAddressType()));
        assertThat(COUNTRY, equalTo(addressDto.getCountry()));
        assertThat(BUILDING_NUMBER, equalTo(addressDto.getBuildingNumber()));
        assertThat(BUILDING_NAME, equalTo(addressDto.getBuildingName()));
        assertThat(CITY, equalTo(addressDto.getCity()));
        assertThat(POSTCODE, equalTo(addressDto.getPostcode()));
        assertThat(UDPRN, equalTo(addressDto.getUdprn()));
        assertThat(PO_BOX, equalTo(addressDto.getPoBox()));
        assertThat(COUNTY, equalTo(addressDto.getCounty()));
    }
}