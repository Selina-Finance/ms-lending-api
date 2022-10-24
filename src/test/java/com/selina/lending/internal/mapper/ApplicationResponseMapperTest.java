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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.selina.lending.internal.dto.DIPApplicationDto;
class ApplicationResponseMapperTest extends MapperBase {

    @Test
    void mapToApplicationResponseDto() {
        //Given
        var applicationResponse = getApplicationResponse();

        //When
        var applicationResponseDto = ApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse);

        //Then
        assertThat(applicationResponseDto.getApplicationId(), equalTo(APPLICATION_ID));
        assertThat(applicationResponseDto.getRequestType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(applicationResponseDto.getApplication(), notNullValue());

        var applicationDto = (DIPApplicationDto) applicationResponseDto.getApplication();

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
        assertThat(applicationDto.getPropertyDetails(), notNullValue());
        assertThat(applicationDto.getPropertyDetails().getPropertyType(), equalTo(PROPERTY_TYPE));
        assertThat(applicationDto.getPropertyDetails().getNumberOfPriorCharges(), equalTo(1));
        assertThat(applicationDto.getPropertyDetails().getPriorCharges().size(), equalTo(1));

        assertPriorCharges(applicationDto);

        assertOffers(applicationDto);
    }

    private void assertPriorCharges(DIPApplicationDto applicationDto) {
        var priorChargesDto = applicationDto.getPropertyDetails().getPriorCharges().get(0);
        assertThat(priorChargesDto.getName(), equalTo(HSBC));
        assertThat(priorChargesDto.getRateType(), equalTo(RATE_TYPE));
        assertThat(priorChargesDto.getRepaymentType(), equalTo(REPAYMENT_TYPE));
        assertThat(priorChargesDto.getMonthlyPayment(), equalTo(MONTHLY_PAYMENT));
    }

    private void assertOffers(DIPApplicationDto applicationDto) {
        var offer = applicationDto.getOffers().get(0);
        assertThat(offer.getProductCode(), equalTo(PRODUCT_CODE));
        assertThat(offer.getMaxErc(), equalTo(MAX_ERC));

        var ercData = offer.getErcData();
        assertThat(ercData.get(0).getPeriod(), equalTo(1));
        assertThat(ercData.get(0).getErcAmount(), equalTo(ERC_AMOUNT));
        assertThat(ercData.get(0).getErcBalance(), equalTo(ERC_BALANCE));
        assertThat(ercData.get(0).getErcFee(), equalTo(ERC_FEE));
        assertThat(ercData.get(1).getPeriod(), equalTo(2));
        assertThat(ercData.get(1).getErcAmount(), equalTo(ERC_AMOUNT));
        assertThat(ercData.get(1).getErcBalance(), equalTo(ERC_BALANCE));
        assertThat(ercData.get(1).getErcFee(), equalTo(ERC_FEE));
    }
}