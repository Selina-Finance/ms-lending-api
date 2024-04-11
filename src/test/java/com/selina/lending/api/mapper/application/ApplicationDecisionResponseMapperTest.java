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

package com.selina.lending.api.mapper.application;

import com.selina.lending.api.dto.application.response.ApplicantResponseDto;
import com.selina.lending.api.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.api.dto.dip.request.AdvancedLoanInformationDto;
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.api.mapper.applicant.ApplicationDecisionResponseMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;

class ApplicationDecisionResponseMapperTest extends MapperBase {

    @Test
    void mapToApplicationDecisionResponseDto() {
        //Given
        var applicationDecisionResponse = getApplicationDecisionResponse();

        //When
        var responseDto = ApplicationDecisionResponseMapper.INSTANCE.mapToApplicationDecisionResponseDto(applicationDecisionResponse);

        //Then
        assertThat(responseDto.getId(), equalTo(APPLICATION_ID));
        assertThat(responseDto.getExternalApplicationId(), equalTo(EXTERNAL_APPLICATION_ID));
        assertThat(responseDto.getApplicants(), notNullValue());
        assertThat(responseDto.getExpenditure(), notNullValue());
        assertThat(responseDto.getExpenditure().get(0).getExpenditureType(), equalTo(EXPENDITURE_TYPE));
        assertThat(responseDto.getUnderwriting().getStageName(), equalTo(UNDERWRITING_STAGE));
        assertThat(responseDto.getIntermediary().getContactFirstName(), equalTo(INTERMEDIARY_FIRSTNAME));
        assertThat(responseDto.getIntermediary().getFcaNumber(), equalTo(FCA_NUMBER));
        assertThat(responseDto.getLead().getUtmCampaign(), equalTo(UTM_CAMPAIGN));
        assertThat(responseDto.getLead().getUtmMedium(), equalTo(UTM_MEDIUM));
        assertThat(responseDto.getLead().getUtmSource(), equalTo(UTM_SOURCE));

        assertOffers(responseDto);
        assertApplicant(responseDto);
        assertLoanInformation(responseDto);
    }

    private void assertOffers(ApplicationDecisionResponse responseDto) {
        var offer = responseDto.getOffers().get(0);
        assertThat(responseDto.getOffers(), notNullValue());
        assertThat(responseDto.getOffers().size(), equalTo(1));
        assertThat(offer.getProductCode(), equalTo(PRODUCT_CODE));
        assertThat(offer.getCategory(), equalTo(CATEGORY_STATUS_0));
        assertThat(offer.getMaxErc(), equalTo(MAX_ERC));
        assertThat(responseDto.getFees().getArrangementFee(), equalTo(ARRANGEMENT_FEE));
        assertThat(offer.getErcData().size(), equalTo(2));
        assertThat(offer.getErcPeriodYears(), equalTo(2));
        assertThat(offer.getMaximumBalanceEsis(), equalTo(MAX_BALANCE_ESIS));

        var ercData = responseDto.getOffers().get(0).getErcData();
        assertThat(ercData.get(0).getPeriod(), equalTo(1));
        assertThat(ercData.get(0).getErcAmount(), equalTo(ERC_AMOUNT));
        assertThat(ercData.get(0).getErcBalance(), equalTo(ERC_BALANCE));
        assertThat(ercData.get(0).getErcFee(), equalTo(ERC_FEE));
        assertThat(ercData.get(1).getPeriod(), equalTo(2));
        assertThat(ercData.get(1).getErcAmount(), equalTo(ERC_AMOUNT));
        assertThat(ercData.get(1).getErcBalance(), equalTo(ERC_BALANCE));
        assertThat(ercData.get(1).getErcFee(), equalTo(ERC_FEE));
    }

    private void assertLoanInformation(ApplicationDecisionResponse responseDto) {
        var advancedLoanInformationDto = (AdvancedLoanInformationDto) responseDto.getLoanInformation();
        assertThat(advancedLoanInformationDto.getLoanPurpose(), equalTo(LOAN_PURPOSE));
        assertThat(advancedLoanInformationDto.getFacilities(), notNullValue());
        assertThat(advancedLoanInformationDto.getFacilities().size(), equalTo(1));
        assertThat(advancedLoanInformationDto.getFacilities().get(0).getAllocationAmount(), equalTo(ALLOCATION_AMOUNT));
        assertThat(advancedLoanInformationDto.getFacilities().get(0).getAllocationPurpose(), equalTo(ALLOCATION_PURPOSE));
    }

    private void assertApplicant(ApplicationDecisionResponse responseDto) {
        var applicantDto = (ApplicantResponseDto) responseDto.getApplicants().get(0);
        assertThat(applicantDto.getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicantDto.getAddresses().size(), equalTo(1));
        assertThat(applicantDto.getEstimatedRetirementAge(), equalTo(ESTIMATED_RETIREMENT_AGE));
        assertThat(applicantDto.getPreviousNames().get(0).getFirstName(), equalTo(FIRST_NAME));
        assertThat(applicantDto.getChecklist().getRequired().getAll(), hasItems(REQUIRED_PASSPORT));

        var documentsDto = applicantDto.getDocuments();
        assertThat(documentsDto.size(), equalTo(1));
        assertThat(documentsDto.get(0).getDocumentType(), equalTo(DOCUMENT_TYPE));

        var creditCheckDto = applicantDto.getCreditCheck();
        assertThat(creditCheckDto.getServiceUsed(), equalTo(CREDIT_CHECK_SERVICE_USED));
        assertThat(creditCheckDto.getCreditScore(), equalTo(CREDIT_SCORE));
        assertThat(creditCheckDto.getCreditCheckReference(), equalTo(CREDIT_CHECK_REF));
        assertThat(creditCheckDto.getHardCheckCompleted(), equalTo(false));
    }
}