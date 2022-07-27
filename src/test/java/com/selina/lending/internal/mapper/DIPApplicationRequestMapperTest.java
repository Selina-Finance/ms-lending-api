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

import com.selina.lending.internal.dto.ApplicationRequest;
import com.selina.lending.internal.dto.DIPApplicationRequest;

public class DIPApplicationRequestMapperTest extends MapperBase{

    @Test
    public void mapToApplicationRequestFromApplicationRequestDto() {
        //Given
        ApplicationRequest applicationRequestDto = getApplicationRequestDto();

        //When
        com.selina.lending.internal.service.application.domain.ApplicationRequest applicationRequest = DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(applicationRequestDto);

        //Then
        assertThat(applicationRequest.getApplicationType(), equalTo(APPLICATION_TYPE));
        assertThat(applicationRequest.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationRequest.getProductCode(), equalTo(PRODUCT_CODE));
        assertThat(applicationRequest.getSource(), equalTo(SOURCE));
        assertThat(applicationRequest.getExpenditure(), notNullValue());
        assertThat(applicationRequest.getExpenditure().size(), equalTo(1));
        assertThat(applicationRequest.getExpenditure().get(0).getExpenditureType(), equalTo(EXPENDITURE_TYPE));
    }

    @Test
    public void mapToApplicationRequestFromDIPApplicationRequestDto() {
        //Given
        DIPApplicationRequest dipApplicationRequest = getDIPApplicationRequestDto();

        //When
        com.selina.lending.internal.service.application.domain.ApplicationRequest applicationRequest = DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);

        //Then
        assertThat(applicationRequest.getApplicationType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(applicationRequest.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationRequest.getProductCode(), equalTo(PRODUCT_CODE));
        assertThat(applicationRequest.getSource(), equalTo(SOURCE));
        assertThat(applicationRequest.getExpenditure(), notNullValue());
        assertThat(applicationRequest.getExpenditure().size(), equalTo(1));
        assertThat(applicationRequest.getExpenditure().get(0).getExpenditureType(), equalTo(EXPENDITURE_TYPE));
        assertThat(applicationRequest.getApplicants().size(), equalTo(1));
        assertThat(applicationRequest.getApplicants().get(0).getIncome(), notNullValue());
        assertThat(applicationRequest.getApplicants().get(0).getIncome().getIncome().size(), equalTo(1));
        assertThat(applicationRequest.getApplicants().get(0).getAddresses().size(), equalTo(1));
        assertThat(applicationRequest.getApplicants().get(0).getEmployment(), notNullValue());
        assertThat(applicationRequest.getLoanInformation().getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(applicationRequest.getLoanInformation().getFacilities().size(), equalTo(1));
        assertThat(applicationRequest.getPropertyDetails(), notNullValue());
        assertThat(applicationRequest.getPropertyDetails().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertThat(applicationRequest.getPropertyDetails().getAddressLine2(), equalTo(ADDRESS_LINE_2));
        assertThat(applicationRequest.getFees(), notNullValue());
        assertThat(applicationRequest.getFees().getArrangementFee(), equalTo(ARRANGEMENT_FEE));
    }
}