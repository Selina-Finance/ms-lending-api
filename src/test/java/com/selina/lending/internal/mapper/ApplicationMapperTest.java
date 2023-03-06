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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

class ApplicationMapperTest extends MapperBase {

    @Test
    void mapToDIPApplicationDto() {
        //Given
        var application = getApplication();

        //When
        var applicationDto = ApplicationMapper.INSTANCE.mapToDIPApplicationDto(application);

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
        assertThat(applicationDto.getPropertyDetails().getEstimatedValue(), equalTo(ESTIMATED_VALUE));
        assertThat(applicationDto.getOffers().get(0).getActive(), equalTo(true));
        assertThat(applicationDto.getOffers().get(0).getId(), equalTo(OFFER_ID));
        assertThat(applicationDto.getOffers().get(0).getProductCode(), equalTo(PRODUCT_CODE));
        assertThat(applicationDto.getOffers().get(0).getAffordabilityDeficit(), equalTo(AFFORDABILITY_DEFICIT));
    }

    @Test
    void mapToApplication() {
        //Given
        var applicationDto = getDIPApplicationDto();

        //When
        var application = ApplicationMapper.INSTANCE.mapToApplication(applicationDto);

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
        assertThat(application.getPropertyDetails().getEstimatedValue(), equalTo(ESTIMATED_VALUE));
        assertThat(application.getOffers().get(0).getActive(), equalTo(true));
        assertThat(application.getOffers().get(0).getId(), equalTo(OFFER_ID));
        assertThat(application.getOffers().get(0).getProductCode(), equalTo(PRODUCT_CODE));
        assertThat(application.getOffers().get(0).getChecklist(), notNullValue());
        assertThat(application.getOffers().get(0).getChecklist().getRequired(), notNullValue());
        assertThat(application.getOffers().get(0).getChecklist().getRequired().getAll(), hasItem(REQUIRED_PASSPORT));
        assertThat(application.getOffers().get(0).getMaxErc(), equalTo(MAX_ERC));
        assertThat(application.getOffers().get(0).getErcData().size(), equalTo(1));
        assertThat(application.getOffers().get(0).getErcData().get(0).getPeriod(), equalTo(1));
    }
}