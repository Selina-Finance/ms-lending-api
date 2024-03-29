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

package com.selina.lending.api.mapper.eligibility;

import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.httpclient.eligibility.dto.request.Applicant;
import com.selina.lending.httpclient.eligibility.dto.request.Client;
import com.selina.lending.httpclient.eligibility.dto.request.CreditRisk;
import com.selina.lending.httpclient.eligibility.dto.request.Decision;
import com.selina.lending.httpclient.eligibility.dto.request.EligibilityRequest;
import com.selina.lending.httpclient.eligibility.dto.request.Income;
import com.selina.lending.httpclient.eligibility.dto.request.PropertyDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
public class EligibilityRequestMapperTest extends MapperBase {

    private static final String PARTNER_ACCOUNT_ID = "somePartnerAccountId";
    private static final String CLIENT_ID = "someClientId";

    @Autowired
    private EligibilityRequestMapper eligibilityRequestMapper;

    @Test
    void shouldMapEligibilityRequest() {
        var products = List.of(getProduct());
        var hasReferOffers = false;
        var eligibilityRequest = eligibilityRequestMapper.mapToPropertyDetails(PARTNER_ACCOUNT_ID, CLIENT_ID, getQuickQuoteApplicationRequestDto(), products, hasReferOffers);

        assertThat(eligibilityRequest, equalTo(EligibilityRequest.builder()
                .extAppId(EXTERNAL_APPLICATION_ID)
                .partnerAccountId(PARTNER_ACCOUNT_ID)
                .decision(Decision.Accept)
                .client(Client.builder()
                        .id(CLIENT_ID)
                        .build())
                .propertyDetails(PropertyDetails.builder()
                        .addressLine1(ADDRESS_LINE_1)
                        .postcode(POSTCODE)
                        .buildingNumber(BUILDING_NUMBER)
                        .buildingName(BUILDING_NAME)
                        .estimatedValue(ESTIMATED_VALUE)
                        .build())
                .applicant(Applicant.builder()
                        .creditRisk(CreditRisk.builder()
                                .filterPassed(FILTER_PASSED_PRE_APPROVAL)
                                .ltv(NET_LTV)
                                .lti(LTI)
                                .dti(DTIR)
                                .build())
                        .incomes(List.of(Income.builder()
                                        .amount(INCOME_AMOUNT)
                                        .type(INCOME_TYPE)
                                        .build()))
                        .build())
                .build()));
    }

    @Test
    void whenHasReferOffersThenPassReferDecision() {
        var products = List.of(getProduct());
        var hasReferOffers = true;
        var eligibilityRequest = eligibilityRequestMapper.mapToPropertyDetails(PARTNER_ACCOUNT_ID, CLIENT_ID, getQuickQuoteApplicationRequestDto(), products, hasReferOffers);

        assertThat(eligibilityRequest.getDecision(), equalTo(Decision.Refer));
    }

    @Test
    void whenHasReferOffersIsNullThenPassAcceptDecision() {
        var products = List.of(getProduct());
        Boolean hasReferOffers = null;
        var eligibilityRequest = eligibilityRequestMapper.mapToPropertyDetails(PARTNER_ACCOUNT_ID, CLIENT_ID, getQuickQuoteApplicationRequestDto(), products, hasReferOffers);

        assertThat(eligibilityRequest.getDecision(), equalTo(Decision.Accept));
    }
}
