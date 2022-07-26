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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.selina.lending.internal.dto.AddressDto;
import com.selina.lending.internal.service.application.domain.Address;

public abstract class MapperBase {

    public static final String FIRST_NAME = "Sally";
    public static final String LAST_NAME = "Smith";
    public static final String EMAIL_ADDRESS = "sally.smith@someemail.com";
    public static final String GENDER = "Female";
    public static final String MOBILE_NUMBER = "07965234654";
    public static final Integer ESTIMATED_RETIREMENT_AGE = 65;
    public static final String NATIONALITY = "British";
    public static final Date DOB;

    static {
        try {
            DOB = new SimpleDateFormat("yyyy-mm-dd").parse("1975-03-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String ADDRESS_LINE_1 = "address line 1";
    public static final String ADDRESS_LINE_2 = "address line 2";
    public static final String ADDRESS_TYPE = "Home";
    public static final String COUNTRY = "England";
    public static final String BUILDING_NUMBER = "10";
    public static final String CITY = "a city";
    public static final String POSTCODE = "postcode";
    public static final int UDPRN = 1235;
    public static final String PO_BOX = "poBox";
    public static final String BUILDING_NAME = "building name";
    public static final String COUNTY = "county";
    public static final Date FROM_DATE = Date.from(Instant.now());
    public static final Date TO_DATE = Date.from(Instant.now());



    protected Address getAddress() {
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
        return address;
    }

    protected List<AddressDto> getAddressDtoList() {
        return List.of(getAddressDto());
    }

    protected AddressDto getAddressDto() {
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
        return addressDto;
    }
}
