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

package com.selina.lending.service.enricher;

import com.selina.lending.httpclient.middleware.dto.common.Address;
import com.selina.lending.httpclient.middleware.dto.common.Applicant;
import com.selina.lending.httpclient.middleware.dto.common.PropertyDetails;
import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import com.selina.lending.service.LendingConstants;
import com.selina.lending.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.selina.lending.service.enricher.MiddlewareRequestEnricher.ADDRESS_TYPE_CURRENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MiddlewareRequestEnricherTest {
    private static final String SOURCE_ACCOUNT = "source1";

    @Mock
    private TokenService tokenService;

    private MiddlewareRequestEnricher enricher;

    @BeforeEach
    void setup() {
        enricher = new MiddlewareRequestEnricher(tokenService);
    }

    @Test
    void enrichCreateDipCCApplicationRequest() {
        //Given
        var request = ApplicationRequest.builder().applicants(List.of(Applicant.builder()
                .primaryApplicant(true)
                .addresses(List.of(Address.builder().addressType(ADDRESS_TYPE_CURRENT).postcode("CODE").build()))
                .build())).propertyDetails(PropertyDetails.builder().postcode("CODE2").build()).build();
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        enricher.enrichCreateDipCCApplicationRequest(request);

        //Then
        assertThat(request.getSourceAccount(), equalTo(SOURCE_ACCOUNT));
        assertThat(request.getSource(), equalTo(LendingConstants.REQUEST_SOURCE));
        assertThat(request.getApplicants().get(0).getIdentifier(), equalTo(1));
        assertThat(request.getIncludeCreditCommitment(), equalTo(true));
        assertThat(request.getProductCode(), equalTo(LendingConstants.PRODUCT_CODE_ALL));
        assertThat(request.getPropertyDetails().getIsApplicantResidence(), equalTo(false));
    }

    @Test
    void enrichCreateDipApplicationRequest() {
        //Given
        var request = ApplicationRequest.builder().applicants(List.of(Applicant.builder()
                .primaryApplicant(true)
                .addresses(List.of(Address.builder().addressType(ADDRESS_TYPE_CURRENT).postcode("CODE").build(),
                        Address.builder().addressType("previous").postcode("oldcode").build()))
                .build(), Applicant.builder().primaryApplicant(false).addresses(List.of(Address.builder().addressType(
                ADDRESS_TYPE_CURRENT).postcode("CODE").build())).build())).propertyDetails(
                PropertyDetails.builder().postcode("CODE").build()).build();
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        enricher.enrichCreateDipApplicationRequest(request);

        //Then
        assertThat(request.getSourceAccount(), equalTo(SOURCE_ACCOUNT));
        assertThat(request.getSource(), equalTo(LendingConstants.REQUEST_SOURCE));
        assertThat(request.getApplicants().get(0).getIdentifier(), equalTo(1));
        assertThat(request.getApplicants().get(1).getIdentifier(), equalTo(2));
        assertThat(request.getIncludeCreditCommitment(), equalTo(false));
        assertThat(request.getProductCode(), equalTo(LendingConstants.PRODUCT_CODE_ALL));
        assertThat(request.getStageOverwrite(), equalTo(LendingConstants.STAGE_OVERWRITE));
        assertThat(request.getPropertyDetails().getIsApplicantResidence(), equalTo(true));
    }

    @Test
    void shouldEnrichCreateQuickQuoteCFRequest() {
        //Given
        var request = QuickQuoteCFRequest.builder().build();

        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        enricher.enrichCreateQuickQuoteCFRequest(request);

        //Then
        assertThat(request.getSourceAccount(), equalTo(SOURCE_ACCOUNT));
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void shouldNotEvaluateIsApplicantResidenceWhenIsApplicantResidencePassedInRequest(boolean isApplicantResidence) {
        //Given
        var request = ApplicationRequest.builder().applicants(List.of(Applicant.builder()
                .primaryApplicant(true)
                .build())).propertyDetails(PropertyDetails.builder().isApplicantResidence(isApplicantResidence).build()).build();
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        enricher.enrichCreateDipCCApplicationRequest(request);

        //Then
        assertThat(request.getPropertyDetails().getIsApplicantResidence(), equalTo(isApplicantResidence));
    }

    @Test
    void shouldNotSetIsApplicantResidenceWhenOnlyOneAddressWithNoAddressType() {
        //Given
        var request = ApplicationRequest.builder().applicants(List.of(Applicant.builder()
                .primaryApplicant(true).addresses(List.of(Address.builder().postcode("CODE").build()))
                .build())).propertyDetails(PropertyDetails.builder().postcode("CODE").build()).build();
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        enricher.enrichCreateDipCCApplicationRequest(request);

        //Then
        assertThat(request.getPropertyDetails().getIsApplicantResidence(), equalTo(true));
    }

    @Test
    void enrichPatchApplicationRequest() {
        //Given
        var request = ApplicationRequest.builder().applicants(
                List.of(Applicant.builder().primaryApplicant(true).build(),
                        Applicant.builder().primaryApplicant(false).build()))
                .propertyDetails(PropertyDetails.builder().isApplicantResidence(true).build()).build();

        //When
        enricher.enrichPatchApplicationRequest(request);

        //Then
        assertThat(request.getRunDecisioning(), equalTo(true));
        assertThat(request.getApplicants().get(0).getIdentifier(), equalTo(1));
        assertThat(request.getApplicants().get(1).getIdentifier(), equalTo(2));
        assertThat(request.getPropertyDetails().getIsApplicantResidence(), equalTo(true));
    }

    @Test
    void shouldNotSetIsApplicantResidenceWhenUnableToDetermineCurrentAddressTypeInAddressList() {
        //Given
        var request = ApplicationRequest.builder().applicants(List.of(Applicant.builder()
                .primaryApplicant(true)
                .addresses(List.of(Address.builder().postcode("CODE").build(),
                        Address.builder().postcode("oldcode").build()))
                .build(), Applicant.builder()
                .primaryApplicant(false)
                .addresses(List.of(Address.builder().postcode("CODE").build()))
                .build())).propertyDetails(PropertyDetails.builder().postcode("CODE").build()).build();
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        enricher.enrichCreateDipApplicationRequest(request);

        //Then
        assertThat(request.getSourceAccount(), equalTo(SOURCE_ACCOUNT));
        assertThat(request.getSource(), equalTo(LendingConstants.REQUEST_SOURCE));
        assertThat(request.getApplicants().get(0).getIdentifier(), equalTo(1));
        assertThat(request.getApplicants().get(1).getIdentifier(), equalTo(2));
        assertThat(request.getIncludeCreditCommitment(), equalTo(false));
        assertThat(request.getProductCode(), equalTo(LendingConstants.PRODUCT_CODE_ALL));
        assertThat(request.getStageOverwrite(), equalTo(LendingConstants.STAGE_OVERWRITE));
        assertThat(request.getPropertyDetails().getIsApplicantResidence(), equalTo(null));
    }
}