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
import com.selina.lending.httpclient.eligibility.dto.request.CreditRisk;
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

    @Autowired
    private EligibilityRequestMapper eligibilityRequestMapper;

    @Test
    void shouldMapEligibilityRequest() {
        var products = List.of(getProduct());
        var eligibilityRequest = eligibilityRequestMapper.mapToPropertyDetails(PARTNER_ACCOUNT_ID, getQuickQuoteApplicationRequestDto(), products);

        assertThat(eligibilityRequest, equalTo(EligibilityRequest.builder()
                .partnerAccountId(PARTNER_ACCOUNT_ID)
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
}
