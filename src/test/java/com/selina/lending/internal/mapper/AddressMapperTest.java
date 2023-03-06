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

package com.selina.lending.internal.mapper;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class AddressMapperTest extends MapperBase {

    @Test
    void mapToAddress() {
        //Given
        var addressDto = getAddressDto();

        // When
        var address = AddressMapper.INSTANCE.mapToAddress(addressDto);

        //Then
        assertThat(address.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(address.getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(address.getAddressType(), equalTo(ADDRESS_TYPE));
        assertThat(address.getCountry(), equalTo(COUNTRY));
        assertThat(address.getBuildingNumber(), equalTo(BUILDING_NUMBER));
        assertThat(address.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(address.getCity(), equalTo(CITY));
        assertThat(address.getPostcode(), equalTo(POSTCODE));
        assertThat(address.getCounty(), equalTo(COUNTY));
        assertThat(address.getUdprn(), equalTo(UDPRN));
        assertThat(address.getPoBox(), equalTo(PO_BOX));
        assertThat(address.getFrom(), equalTo(FROM_DATE));
    }

    @Test
    void mapToAddressDto() {
        //Given
        var address = getAddress();

        // When
        var addressDto = AddressMapper.INSTANCE.mapToAddressDto(address);

        //Then
        assertThat(addressDto.getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(addressDto.getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(addressDto.getAddressType(), equalTo(ADDRESS_TYPE));
        assertThat(addressDto.getCountry(), equalTo(COUNTRY));
        assertThat(addressDto.getBuildingNumber(), equalTo(BUILDING_NUMBER));
        assertThat(addressDto.getBuildingName(), equalTo(BUILDING_NAME));
        assertThat(addressDto.getCity(), equalTo(CITY));
        assertThat(addressDto.getPostcode(), equalTo(POSTCODE));
        assertThat(addressDto.getUdprn(), equalTo(UDPRN));
        assertThat(addressDto.getPoBox(), equalTo(PO_BOX));
        assertThat(addressDto.getCounty(), equalTo(COUNTY));
        assertThat(addressDto.getFromDate(), equalTo(FROM_DATE));
    }
}