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

package com.selina.lending.api.mapper.dip;

import com.selina.lending.api.dto.dip.response.DIPApplicationDto;
import com.selina.lending.api.dto.dipcc.response.DIPCCApplicationResponse;
import com.selina.lending.api.dto.creditcommitments.response.DetailResponseDto;
import com.selina.lending.api.mapper.dipcc.DIPCCApplicationResponseMapper;
import com.selina.lending.api.mapper.MapperBase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import static com.selina.lending.service.LendingConstants.ACCEPT_DECISION;

class ApplicationResponseMapperTest extends MapperBase {

    @Test
    void mapToDIPCCApplicationResponseDto() {
        //Given
        var applicationResponse = getApplicationResponse();

        //When
        var applicationResponseDto = DIPCCApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse);

        //Then
        assertThat(applicationResponseDto.getApplicationId(), equalTo(APPLICATION_ID));
        assertThat(applicationResponseDto.getRequestType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(applicationResponseDto.getApplication(), notNullValue());

        var applicationDto = (DIPApplicationDto) applicationResponseDto.getApplication();

        assertThat(applicationDto.getId(), equalTo(APPLICATION_ID));
        assertThat(applicationDto.getDecision(), equalTo(ACCEPT_DECISION));
        assertThat(applicationDto.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationDto.getRequestType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(applicationDto.getApplicants(), notNullValue());
        assertThat(applicationDto.getApplicants().size(), equalTo(1));
        assertThat(applicationDto.getApplicants().get(0).getAddresses().size(), equalTo(1));
        assertThat(applicationDto.getLoanInformation(), notNullValue());
        assertThat(applicationDto.getLoanInformation().getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(applicationDto.getPropertyDetails(), notNullValue());

        assertOffers(applicationDto);

        assertCreditCommitment(applicationResponseDto);
    }

    @Test
    void mapToDIPApplicationResponseDto() {
        //Given
        var applicationResponse = getApplicationResponse();

        //When
        var applicationResponseDto = DIPApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse);

        //Then
        assertThat(applicationResponseDto.getApplicationId(), equalTo(APPLICATION_ID));
        assertThat(applicationResponseDto.getRequestType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(applicationResponseDto.getApplication(), notNullValue());

        var applicationDto = (DIPApplicationDto) applicationResponseDto.getApplication();

        assertThat(applicationDto.getId(), equalTo(APPLICATION_ID));
        assertThat(applicationDto.getDecision(), equalTo(ACCEPT_DECISION));
        assertThat(applicationDto.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(applicationDto.getRequestType(), equalTo(DIP_APPLICATION_TYPE));
        assertThat(applicationDto.getApplicants(), notNullValue());
        assertThat(applicationDto.getApplicants().size(), equalTo(1));
        assertThat(applicationDto.getApplicants().get(0).getAddresses().size(), equalTo(1));
        assertThat(applicationDto.getLoanInformation(), notNullValue());
        assertThat(applicationDto.getLoanInformation().getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(applicationDto.getPropertyDetails(), notNullValue());

        assertOffers(applicationDto);
    }

    private void assertCreditCommitment(DIPCCApplicationResponse response) {
        var creditCommitment = response.getCreditCommitment();
        assertThat(creditCommitment.getApplicants().size(), equalTo(1));
        assertThat(creditCommitment.getApplicants().get(0).getPrimaryApplicant(), equalTo(true));
        assertThat(creditCommitment.getApplicants().get(0).getCreditScore(), equalTo(CREDIT_SCORE));

        var commitmentDetails = creditCommitment.getApplicants().get(0).getCreditCommitments();
        assertDetail(commitmentDetails.getSystem().getDetail().get(0));
        assertDetail(commitmentDetails.getUser().getDetail().get(0));

        assertThat(commitmentDetails.getSystem().getSummary().getNumberAccounts(), equalTo(2));
        assertThat(commitmentDetails.getSystem().getSummary().getOutstandingBalance(), equalTo(OUTSTANDING_BALANCE));
    }

    private void assertDetail(DetailResponseDto detailDto) {
        assertThat(detailDto.getId(), equalTo(DETAIL_ID));
        assertThat(detailDto.getStatus(), equalTo(STATUS));
    }

    private void assertOffers(DIPApplicationDto applicationDto) {
        var offer = applicationDto.getOffers().get(0);
        assertThat(offer.getProductCode(), equalTo(PRODUCT_CODE));
        assertThat(offer.getMaxErc(), equalTo(MAX_ERC));
        assertThat(offer.getErcShortCode(), equalTo(ERC_SHORT_CODE));
        assertThat(offer.getErcPeriodYears(), equalTo(2));
        assertThat(offer.getMaximumBalanceEsis(), equalTo(MAX_BALANCE_ESIS));

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