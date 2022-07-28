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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.selina.lending.internal.dto.DIPApplicationDto;
import com.selina.lending.internal.service.application.domain.Application;

public class ApplicationMapperTest extends MapperBase {

    @Test
    public void mapToDIPApplicationDto() {
        //Given
        Application application = getApplication();

        //When
        DIPApplicationDto applicationDto = ApplicationMapper.INSTANCE.mapToDIPApplicationDto(application);

        //Then
        assertThat(applicationDto.getId(), equalTo(APPLICATION_ID));
        assertThat(applicationDto.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationDto.getApplicants().size(), equalTo(1));
        assertThat(applicationDto.getApplicants().get(0).getTitle(), equalTo(TITLE));
        assertThat(applicationDto.getApplicants().get(0).getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicationDto.getApplicants().get(0).getLastName(), equalTo(LAST_NAME));
        assertThat(applicationDto.getRequestType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(applicationDto.getLoanInformation().getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(applicationDto.getLoanInformation().getRequestedLoanAmount(), equalTo(LOAN_AMOUNT));
        assertThat(applicationDto.getLoanInformation().getFacilities().size(), equalTo(1));
        assertThat(applicationDto.getLoanInformation().getFacilities().get(0).getAllocationAmount(), equalTo(ALLOCATION_AMOUNT));
        assertThat(applicationDto.getLoanInformation().getFacilities().get(0).getAllocationPurpose(), equalTo(ALLOCATION_PURPOSE));
        assertThat(applicationDto.getPropertyDetails().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(applicationDto.getPropertyDetails().getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(applicationDto.getOffers().get(0).getActive(), equalTo(true));
        assertThat(applicationDto.getOffers().get(0).getId(), equalTo(OFFER_ID));
        assertThat(applicationDto.getOffers().get(0).getProductCode(), equalTo(PRODUCT_CODE));
    }

    @Test
    public void mapToApplication() {
        //Given
        DIPApplicationDto applicationDto = getDIPApplicationDto();

        //When
        Application application = ApplicationMapper.INSTANCE.mapToApplication(applicationDto);

        //Then
        assertThat(application.getId(), equalTo(APPLICATION_ID));
        assertThat(application.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(application.getApplicants().size(), equalTo(1));
        assertThat(application.getApplicants().get(0).getTitle(), equalTo(TITLE));
        assertThat(application.getApplicants().get(0).getFirstName(), equalTo(FIRST_NAME));
        assertThat(application.getApplicants().get(0).getLastName(), equalTo(LAST_NAME));
        assertThat(application.getApplicationType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(application.getLoanInformation().getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(application.getLoanInformation().getRequestedLoanAmount(), equalTo(LOAN_AMOUNT));
        assertThat(application.getLoanInformation().getFacilities().size(), equalTo(1));
        assertThat(application.getPropertyDetails().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(application.getPropertyDetails().getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(application.getOffers().get(0).getActive(), equalTo(true));
        assertThat(application.getOffers().get(0).getId(), equalTo(OFFER_ID));
        assertThat(application.getOffers().get(0).getProductCode(), equalTo(PRODUCT_CODE));
        assertThat(application.getOffers().get(0).getChecklist(), notNullValue());
        assertThat(application.getOffers().get(0).getChecklist().getRequired(), notNullValue());
        assertThat(application.getOffers().get(0).getChecklist().getRequired().getAll(), hasItem(REQUIRED_PASSPORT));
    }
}