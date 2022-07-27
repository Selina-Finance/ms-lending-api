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
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.selina.lending.internal.dto.DIPApplicationDto;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

public class ApplicationResponseMapperTest extends MapperBase {

    @Test
    public void mapToApplicationResponseDto() {
        //Given
        ApplicationResponse applicationResponse = getApplicationResponse();

        //When
        com.selina.lending.internal.dto.ApplicationResponse applicationResponseDto = ApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse);

        //Then
        assertThat(applicationResponseDto.getApplicationId(), equalTo(APPLICATION_ID));
        assertThat(applicationResponseDto.getRequestType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(applicationResponseDto.getApplication(), notNullValue());

        DIPApplicationDto applicationDto = (DIPApplicationDto) applicationResponseDto.getApplication();

        assertThat(applicationDto.getId(), equalTo(APPLICATION_ID));
        assertThat(applicationDto.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationDto.getRequestType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(applicationDto.getApplicants(), notNullValue());
        assertThat(applicationDto.getApplicants().size(), equalTo(1));
        assertThat(applicationDto.getApplicants().get(0).getAddresses().size(), equalTo(1));
        assertThat(applicationDto.getLoanInformation(), notNullValue());
        assertThat(applicationDto.getLoanInformation().getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(applicationDto.getLoanInformation().getFacilities(), notNullValue());
        assertThat(applicationDto.getLoanInformation().getFacilities().size(), equalTo(1));
        assertThat(applicationDto.getOffers(), notNullValue());
        assertThat(applicationDto.getOffers().size(), equalTo(1));
        assertThat(applicationDto.getPropertyDetails(), notNullValue());
        assertThat(applicationDto.getPropertyDetails().getPropertyType(), equalTo(PROPERTY_TYPE));
    }

}