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

package com.selina.lending.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.selina.lending.service.FilterApplicationServiceImpl.ADP_CLIENT_ID;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.selina.lending.api.dto.common.LeadDto;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.dto.qq.request.QuickQuoteFeesDto;
import com.selina.lending.api.dto.qq.response.ProductOfferDto;
import com.selina.lending.api.dto.qq.response.QuickQuoteResponse;
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.httpclient.adp.dto.request.QuickQuoteEligibilityApplicationRequest;
import com.selina.lending.httpclient.adp.dto.response.QuickQuoteEligibilityDecisionResponse;
import com.selina.lending.httpclient.adp.dto.response.Product;
import com.selina.lending.httpclient.eligibility.dto.response.EligibilityResponse;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import com.selina.lending.httpclient.selection.dto.request.FilterQuickQuoteApplicationRequest;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.repository.AdpGatewayRepository;
import com.selina.lending.repository.EligibilityRepository;
import com.selina.lending.repository.MiddlewareRepository;
import com.selina.lending.repository.SelectionRepository;
import com.selina.lending.service.quickquote.ArrangementFeeSelinaService;
import com.selina.lending.service.quickquote.PartnerService;

@SpringBootTest
class FilterApplicationServiceImplTest extends MapperBase {
    @MockBean
    private AdpGatewayRepository adpGatewayRepository;

    @MockBean
    private SelectionRepository selectionRepository;

    @MockBean
    private MiddlewareRepository middlewareRepository;

    @MockBean
    private EligibilityRepository eligibilityRepository;

    @MockBean
    private ArrangementFeeSelinaService arrangementFeeSelinaService;

    @MockBean
    private PartnerService partnerService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private FilterApplicationServiceImpl filterApplicationService;

    @Test
    void shouldFilterQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequestWithCorrectDefaultValues() {
        // Given
        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);
        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        //When
        var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

        //Then
        verify(selectionRepository, times(1)).filter(selectionRequestCaptor.capture());
        verify(middlewareRepository, times(1)).createQuickQuoteApplication(any());
        verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();

        assertThat(response.getStatus()).isEqualTo(decisionResponse.getDecision());

        var requestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(requestFees.getIsAddArrangementFeeSelinaToLoan()).isFalse();
        assertThat(requestFees.getIsAddProductFeesToFacility()).isFalse();
    }

    @Test
    void shouldFilterQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequestWithoutDefaultValuesIfValuesPresent() {
        // Given
        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);
        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        var qqFees = QuickQuoteFeesDto.builder()
                .isAddArrangementFeeSelinaToLoan(true)
                .isAddProductFeesToFacility(true)
                .build();

        var qqApplicationRequest = QuickQuoteApplicationRequest.builder()
                .applicants(List.of())
                .loanInformation(getQQLoanInformationDto())
                .propertyDetails(getQuickQuotePropertyDetailsDto())
                .fees(qqFees)
                .build();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(qqApplicationRequest, decisionResponse.getProducts())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        //When
        var response = filterApplicationService.filter(qqApplicationRequest);

        //Then
        verify(selectionRepository, times(1)).filter(selectionRequestCaptor.capture());
        verify(middlewareRepository, times(1)).createQuickQuoteApplication(any());
        verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();

        assertThat(response.getStatus()).isEqualTo(decisionResponse.getDecision());

        var requestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(requestFees.getIsAddArrangementFeeSelinaToLoan()).isEqualTo(qqFees.getIsAddArrangementFeeSelinaToLoan());
        assertThat(requestFees.getIsAddProductFeesToFacility()).isEqualTo(qqFees.getIsAddProductFeesToFacility());
    }

    @Test
    void shouldFilterQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequestWithPartnerAdded() {
        // Given
        var quickQuoteRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteRequest.setPartner(null);

        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(getPartner());

        //When
        var response = filterApplicationService.filter(quickQuoteRequest);

        //Then
        verify(selectionRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
        verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();
        verify(partnerService, times(1)).getPartnerFromToken();
        assertThat(quickQuoteRequest.getPartner().getSubUnitId()).isEqualTo(SUB_UNIT_ID);
        assertThat(quickQuoteRequest.getPartner().getCompanyId()).isEqualTo(COMPANY_ID);
        assertThat(response.getStatus()).isEqualTo(decisionResponse.getDecision());
    }

    @Test
    void whenHaveOneApplicantWithPrimaryApplicantNullThenApplicantPrimaryApplicantIsTrue() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(null);
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
    }

    @Test
    void whenHaveOneApplicantWithPrimaryApplicantTrueThenApplicantPrimaryApplicantIsTrue() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(true);
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
    }

    @Test
    void whenHaveOneApplicantPrimaryApplicantTrueAndOneApplicantPrimaryApplicantFalseThenApplicantPrimaryApplicantIsTrue() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());
        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(true);
        quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(false);
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
        assertFalse(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
    }

    @Test
    void whenHaveOneApplicantPrimaryApplicantTrueAndOneApplicantPrimaryApplicantNullThenFirstApplicantPrimaryApplicantIsTrue() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());
        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(true);
        quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(null);
        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
        assertNull(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
    }

    @Test
    void whenHaveOneApplicantPrimaryApplicantNullAndOneApplicantPrimaryApplicantTrueThenSecondApplicantPrimaryApplicantIsTrue() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());
        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(null);
        quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(true);

        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        assertNull(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
        assertTrue(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
    }

    @Test
    void whenHaveOneApplicantPrimaryApplicantFalseAndOneApplicantPrimaryApplicantTrueThenSecondApplicantPrimaryApplicantIsTrue() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());
        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(false);
        quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(true);

        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        assertFalse(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
        assertTrue(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
    }

    @Test
    void whenHaveTwoApplicantPrimaryApplicantNullThenFirstApplicantPrimaryApplicantIsTrue() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());

        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(null);
        quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(null);

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
        assertNull(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
    }

    @Test
    void whenDecisionResponseIsDeclinedThenDoNotSendMiddlewareRequest() {
        //Given
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Declined")
                .products(List.of(getProduct()))
                .build();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());

        //When
        var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

        //Then
        verify(selectionRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
        verify(middlewareRepository, times(0)).createQuickQuoteApplication(any(QuickQuoteRequest.class));
        assertThat(response.getStatus()).isEqualTo("Declined");
        assertThat(response.getOffers()).hasSize(decisionResponse.getProducts().size());
    }

    @Test
    void whenDecisionResponseHasNullProductOffersThenDoNotSendMiddlewareRequest() {
        //Given
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Declined")
                .products(null)
                .build();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());

        //When
        var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

        //Then
        verify(selectionRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
        verify(middlewareRepository, times(0)).createQuickQuoteApplication(any(QuickQuoteRequest.class));
        assertThat(response.getStatus()).isEqualTo("Declined");
    }

    @Test
    void shouldCreateApplicationRequestWithFeesIfProvidedButNoArrangementFeeSelinaFields() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();
        var decisionResponse = getFilteredQuickQuoteDecisionResponse();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        assertThat(quickQuoteApplicationRequest.getFees().getArrangementFee()).isEqualTo(1000.00);
        assertThat(quickQuoteApplicationRequest.getFees().getAdviceFee()).isEqualTo(599.00);
        assertTrue(quickQuoteApplicationRequest.getFees().getIsAddAdviceFeeToLoan());
    }

    @Test
    void whenFeesAreNotSpecifiedThenDoNotIncludeArrangementFeeSelinaAndProductFeeToTheLoan() {
        // Given
        var declinedDecisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Declined")
                .products(null)
                .build();

        when(tokenService.retrieveClientId()).thenReturn("some-aggregator");
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(declinedDecisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        verify(selectionRepository).filter(selectionRequestCaptor.capture());
        var selectionRequestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(selectionRequestFees.getIsAddProductFeesToFacility()).isFalse();
        assertThat(selectionRequestFees.getIsAddArrangementFeeSelinaToLoan()).isFalse();
    }

    @Test
    void shouldUseSpecifiedFeesForIncludeArrangementFeeSelinaAndProductFeeToTheLoan() {
        // Given
        var declinedDecisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Declined")
                .products(null)
                .build();

        when(tokenService.retrieveClientId()).thenReturn("some-aggregator");
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(declinedDecisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();
        quickQuoteApplicationRequest.getFees().setIsAddArrangementFeeSelinaToLoan(false);
        quickQuoteApplicationRequest.getFees().setIsAddProductFeesToFacility(true);

        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        verify(selectionRepository).filter(selectionRequestCaptor.capture());
        var selectionRequestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(selectionRequestFees.getIsAddArrangementFeeSelinaToLoan()).isFalse();
        assertThat(selectionRequestFees.getIsAddProductFeesToFacility()).isTrue();
    }

    @Test
    void whenClientIsMonevoAndFeesAreNotSpecifiedThenIncludeArrangementFeeSelinaAndProductFeeToTheLoan() {
        // Given
        var declinedDecisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Declined")
                .products(null)
                .build();

        when(tokenService.retrieveClientId()).thenReturn("monevo");
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(declinedDecisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        verify(selectionRepository).filter(selectionRequestCaptor.capture());
        var selectionRequestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(selectionRequestFees.getIsAddProductFeesToFacility()).isTrue();
        assertThat(selectionRequestFees.getIsAddArrangementFeeSelinaToLoan()).isTrue();
    }

    @Test
    void whenClientIsMonevoAndFeesAreSpecifiedThenOverwriteThemToIncludeArrangementFeeSelinaAndProductFeeToTheLoan() {
        // Given
        var declinedDecisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Declined")
                .products(null)
                .build();

        when(tokenService.retrieveClientId()).thenReturn("monevo");
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(declinedDecisionResponse);
        when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();
        quickQuoteApplicationRequest.getFees().setIsAddArrangementFeeSelinaToLoan(false);
        quickQuoteApplicationRequest.getFees().setIsAddProductFeesToFacility(false);

        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        verify(selectionRepository).filter(selectionRequestCaptor.capture());
        var selectionRequestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(selectionRequestFees.getIsAddProductFeesToFacility()).isTrue();
        assertThat(selectionRequestFees.getIsAddArrangementFeeSelinaToLoan()).isTrue();
    }

    @Test
    void whenClientIsClearScoreAndFeesAreNotSpecifiedThenIncludeArrangementFeeSelinaAndProductFeeToTheLoan() {
        // Given
        var declinedDecisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Declined")
                .products(null)
                .build();

        when(tokenService.retrieveClientId()).thenReturn("clearscore");
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(declinedDecisionResponse);
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        verify(selectionRepository).filter(selectionRequestCaptor.capture());
        var selectionRequestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(selectionRequestFees.getIsAddProductFeesToFacility()).isTrue();
        assertThat(selectionRequestFees.getIsAddArrangementFeeSelinaToLoan()).isTrue();
    }

    @Test
    void whenClientIsClearScoreAndFeesAreSpecifiedThenOverwriteThemToIncludeArrangementFeeSelinaAndProductFeeToTheLoan() {
        // Given
        var declinedDecisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Declined")
                .products(null)
                .build();

        when(tokenService.retrieveClientId()).thenReturn("clearscore");
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(declinedDecisionResponse);
        var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();
        quickQuoteApplicationRequest.getFees().setIsAddArrangementFeeSelinaToLoan(false);
        quickQuoteApplicationRequest.getFees().setIsAddProductFeesToFacility(false);

        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        verify(selectionRepository).filter(selectionRequestCaptor.capture());
        var selectionRequestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(selectionRequestFees.getIsAddProductFeesToFacility()).isTrue();
        assertThat(selectionRequestFees.getIsAddArrangementFeeSelinaToLoan()).isTrue();
    }

    @Nested
    class AdpDecisioning {

        @Test
        void shouldCreateQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequestWithCorrectDefaultValues() {
            // Given
            var argumentCaptor = ArgumentCaptor.forClass(QuickQuoteEligibilityApplicationRequest.class);
            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            //When
            var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

            //Then
            verify(adpGatewayRepository, times(1)).quickQuoteEligibility(argumentCaptor.capture());
            verify(middlewareRepository, times(1)).createQuickQuoteApplication(any());
            verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();

            assertThat(response.getStatus()).isEqualTo(decisionResponse.getDecision());

            var requestFees = argumentCaptor.getValue().getApplication().getFees();
            assertThat(requestFees.getIsAddArrangementFeeSelinaToLoan()).isFalse();
            assertThat(requestFees.getIsAddProductFeesToFacility()).isFalse();
        }

        @Test
        void shouldCreateQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequestWithoutDefaultValuesIfValuesPresent() {
            // Given
            var argumentCaptor = ArgumentCaptor.forClass(QuickQuoteEligibilityApplicationRequest.class);
            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            var qqFees = QuickQuoteFeesDto.builder()
                    .isAddArrangementFeeSelinaToLoan(true)
                    .isAddProductFeesToFacility(true)
                    .build();

            var qqApplicationRequest = QuickQuoteApplicationRequest.builder()
                    .applicants(List.of())
                    .loanInformation(getQQLoanInformationDto())
                    .propertyDetails(getQuickQuotePropertyDetailsDto())
                    .fees(qqFees)
                    .build();

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(qqApplicationRequest, decisionResponse.getProducts())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            //When
            var response = filterApplicationService.filter(qqApplicationRequest);

            //Then
            verify(adpGatewayRepository, times(1)).quickQuoteEligibility(argumentCaptor.capture());
            verify(middlewareRepository, times(1)).createQuickQuoteApplication(any());
            verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();

            assertThat(response.getStatus()).isEqualTo(decisionResponse.getDecision());

            var requestFees = argumentCaptor.getValue().getApplication().getFees();
            assertThat(requestFees.getIsAddArrangementFeeSelinaToLoan()).isEqualTo(qqFees.getIsAddArrangementFeeSelinaToLoan());
            assertThat(requestFees.getIsAddProductFeesToFacility()).isEqualTo(qqFees.getIsAddProductFeesToFacility());
        }

        @Test
        void shouldCreateQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequestWithPartnerAdded() {
            // Given
            var quickQuoteRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteRequest.setPartner(null);

            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(getPartner());

            //When
            var response = filterApplicationService.filter(quickQuoteRequest);

            //Then
            verify(adpGatewayRepository, times(1)).quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class));
            verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();
            verify(partnerService, times(1)).getPartnerFromToken();
            assertThat(quickQuoteRequest.getPartner().getSubUnitId()).isEqualTo(SUB_UNIT_ID);
            assertThat(quickQuoteRequest.getPartner().getCompanyId()).isEqualTo(COMPANY_ID);
            assertThat(response.getStatus()).isEqualTo(decisionResponse.getDecision());
        }

        @Test
        void whenHaveOneApplicantWithPrimaryApplicantNullThenApplicantPrimaryApplicantIsTrue() {
            // Given
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(null);

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            // When
            filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
        }

        @Test
        void whenHaveOneApplicantWithPrimaryApplicantTrueThenApplicantPrimaryApplicantIsTrue() {
            // Given
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(true);

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            // When
            filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
        }

        @Test
        void whenHaveOneApplicantPrimaryApplicantTrueAndOneApplicantPrimaryApplicantFalseThenApplicantPrimaryApplicantIsTrue() {
            // Given
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());
            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(true);
            quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(false);

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            // When
            filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
            assertFalse(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
        }

        @Test
        void whenHaveOneApplicantPrimaryApplicantTrueAndOneApplicantPrimaryApplicantNullThenFirstApplicantPrimaryApplicantIsTrue() {
            // Given
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());
            quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(true);
            quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(null);
            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            // When
            filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
            assertNull(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
        }

        @Test
        void whenHaveOneApplicantPrimaryApplicantNullAndOneApplicantPrimaryApplicantTrueThenSecondApplicantPrimaryApplicantIsTrue() {
            // Given
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());
            quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(null);
            quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(true);

            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            // When
            filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertNull(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
            assertTrue(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
        }

        @Test
        void whenHaveOneApplicantPrimaryApplicantFalseAndOneApplicantPrimaryApplicantTrueThenSecondApplicantPrimaryApplicantIsTrue() {
            // Given
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());
            quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(false);
            quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(true);

            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            // When
            filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertFalse(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
            assertTrue(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
        }

        @Test
        void whenHaveTwoApplicantPrimaryApplicantNullThenFirstApplicantPrimaryApplicantIsTrue() {
            // Given
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getApplicants().add(getQuickQuoteApplicantDto());

            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(null);
            quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(null);

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            // When
            filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertTrue(quickQuoteApplicationRequest.getApplicants().get(0).getPrimaryApplicant());
            assertNull(quickQuoteApplicationRequest.getApplicants().get(1).getPrimaryApplicant());
        }

        @Test
        void whenDecisionResponseIsDeclinedThenDoNotSendMiddlewareRequest() {
            //Given
            var decisionResponse = QuickQuoteEligibilityDecisionResponse.builder()
                    .decision("Declined")
                    .products(List.of(getProduct()))
                    .build();

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());

            //When
            var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

            //Then
            verify(adpGatewayRepository, times(1)).quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class));
            verify(middlewareRepository, times(0)).createQuickQuoteApplication(any(QuickQuoteRequest.class));
            assertThat(response.getStatus()).isEqualTo("Declined");
            assertThat(response.getOffers()).hasSize(decisionResponse.getProducts().size());
        }

        @Test
        void whenDecisionResponseHasNullProductOffersThenDoNotSendMiddlewareRequest() {
            //Given
            var decisionResponse = QuickQuoteEligibilityDecisionResponse.builder()
                    .decision("Declined")
                    .products(null)
                    .build();

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());

            //When
            var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

            //Then
            verify(adpGatewayRepository, times(1)).quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class));
            verify(middlewareRepository, times(0)).createQuickQuoteApplication(any(QuickQuoteRequest.class));
            assertThat(response.getStatus()).isEqualTo("Declined");
        }
        @Test
        void shouldCreateApplicationRequestWithFeesIfProvidedButNoArrangementFeeSelinaFields() {
            // Given
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();
            var decisionResponse = getQuickQuoteEligibilityDecisionResponse();

            when(tokenService.retrieveClientId()).thenReturn(ADP_CLIENT_ID);
            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(adpGatewayRepository.quickQuoteEligibility(any(QuickQuoteEligibilityApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());

            // When
            filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertThat(quickQuoteApplicationRequest.getFees().getArrangementFee()).isEqualTo(1000.00);
            assertThat(quickQuoteApplicationRequest.getFees().getAdviceFee()).isEqualTo(599.00);
            assertTrue(quickQuoteApplicationRequest.getFees().getIsAddAdviceFeeToLoan());
        }
    }

    @Nested
    class Eligibility {

        @Test
        void shouldEnrichResponseOffersWithEligibility() {
            // Given
            var eligibilityValue = 95.1;
            var eligibility = EligibilityResponse.builder()
                    .eligibility(eligibilityValue)
                    .build();
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();
            var decisionResponse = getFilteredQuickQuoteDecisionResponse();

            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(quickQuoteApplicationRequest, decisionResponse.getProducts())).thenReturn(eligibility);

            // When
            var quickQuoteResponse = filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertThat(quickQuoteResponse.getOffers().get(0).getEligibility()).isEqualTo(eligibilityValue);
        }

        @Test
        void shouldSendToMiddlewareSpecifiedPropertyDetailsEstimatedValue() {
            // Given
            var qqMiddlewareRequestCaptor = ArgumentCaptor.forClass(QuickQuoteRequest.class);
            var decisionResponse = getFilteredQuickQuoteDecisionResponse();

            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            //When
            var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

            //Then
            verify(middlewareRepository, times(1)).createQuickQuoteApplication(qqMiddlewareRequestCaptor.capture());

            assertThat(qqMiddlewareRequestCaptor.getValue().getPropertyDetails().getEstimatedValue()).isEqualTo(ESTIMATED_VALUE);
        }

        @Test
        void whenPropertyDetailsEstimatedValueIsNotSpecifiedThenSendToMiddlewareEligibilityPropertyInfoEstimatedValue() {
            // Given
            var applicationRequest = getQuickQuoteApplicationRequestDto();
            applicationRequest.getPropertyDetails().setEstimatedValue(null);

            var qqMiddlewareRequestCaptor = ArgumentCaptor.forClass(QuickQuoteRequest.class);
            var decisionResponse = getFilteredQuickQuoteDecisionResponse();

            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(null);

            //When
            filterApplicationService.filter(applicationRequest);

            //Then
            verify(middlewareRepository, times(1)).createQuickQuoteApplication(qqMiddlewareRequestCaptor.capture());

            assertThat(qqMiddlewareRequestCaptor.getValue().getPropertyDetails().getEstimatedValue()).isEqualTo(ELIGIBILITY_ESTIMATED_VALUE);
        }

        @Test
        void whenGetExceptionRequestingEligibilityServiceThenReturnOffersWithDefaultEligibility() {
            // Given
            QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();
            var decisionResponse = getFilteredQuickQuoteDecisionResponse();

            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(quickQuoteApplicationRequest, decisionResponse.getProducts())).thenThrow(RuntimeException.class);

            // When
            var quickQuoteResponse = filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertThat(quickQuoteResponse.getOffers().get(0).getEligibility()).isEqualTo(ELIGIBILITY);
        }
    }

    @Nested
    class AlternativeOffer {

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4})
        void whenRequestedLoanTermIsLessThan5ThenReturnDeclinedResponse(int requestedLoanTerm) {
            // Given
            var declinedDecisionResponse = QuickQuoteResponse.builder()
                    .status("Declined")
                    .offers(null)
                    .build();

            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);

            when(tokenService.retrieveClientId()).thenReturn("some-aggregator");

            // When
            var decisionResponse = filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertThat(decisionResponse).isEqualTo(declinedDecisionResponse);
            verify(selectionRepository, never()).filter(any(FilterQuickQuoteApplicationRequest.class));
            verify(eligibilityRepository, never()).getEligibility(any(QuickQuoteApplicationRequest.class), anyList());
            verify(middlewareRepository, never()).createQuickQuoteApplication(any(QuickQuoteRequest.class));
        }

        @Nested
        class Monevo {

            private static final LeadDto CREDIT_KARMA_PARTNER_UTM = LeadDto.builder()
                    .utmSource("aggregator")
                    .utmMedium("cpc")
                    .utmCampaign("_consumer_referral___creditkarma_main_")
                    .build();

            @ParameterizedTest
            @ValueSource(ints = {1, 2, 3, 4})
            void whenClientIsMonevoAndPartnerIsCreditKarmaAndRequestedLoanTermIsLessThan5ThenReturnDeclinedResponse(int requestedLoanTerm) {
                // Given
                var declinedDecisionResponse = QuickQuoteResponse.builder()
                        .status("Declined")
                        .offers(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(
                        FilteredQuickQuoteDecisionResponse.builder().build());

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);
                quickQuoteApplicationRequest.setLead(CREDIT_KARMA_PARTNER_UTM);

                when(tokenService.retrieveClientId()).thenReturn("monevo");

                // When
                var decisionResponse = filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                assertThat(decisionResponse).isEqualTo(declinedDecisionResponse);
                verify(selectionRepository, never()).filter(any(FilterQuickQuoteApplicationRequest.class));
                verify(middlewareRepository, never()).createQuickQuoteApplication(any(QuickQuoteRequest.class));
            }

            @ParameterizedTest
            @ValueSource(ints = {5, 7, 9, 11, 15, 30})
            void whenClientIsMonevoAndPartnerIsCreditKarmaThenKeepOriginalRequestedLoanTermValue(int requestedLoanTerm) {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);
                quickQuoteApplicationRequest.setLead(CREDIT_KARMA_PARTNER_UTM);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("monevo");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(requestedLoanTerm);
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2, 3, 4})
            void whenClientIsMonevoAndRequestedLoanTermIsBetween1and4ThenAdjustItTo5(int requestedLoanTerm) {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("monevo");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            }

            @ParameterizedTest
            @ValueSource(ints = {5, 6, 7, 8, 9, 10})
            void whenClientIsMonevoAndRequestedLoanTermIsBetween5and10ThenAdjustItTo10(int requestedLoanTerm) {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("monevo");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(10);
            }

            @ParameterizedTest
            @ValueSource(ints = {11, 15, 30})
            void whenClientIsMonevoAndRequestedLoanTermIsGreaterThan10ThenLeaveOriginalValue(int requestedLoanTerm) {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.setLead(CREDIT_KARMA_PARTNER_UTM);
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("monevo");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(requestedLoanTerm);
            }
        }

        @Nested
        class Experian {

            @ParameterizedTest
            @ValueSource(ints = {1, 2, 3, 4, 5})
            void whenClientIsExperianAndRequestedLoanTermIsBetween1And5ThenAdjustItTo5(int requestedLoanTerm) {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("experian");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            }

            @ParameterizedTest
            @ValueSource(ints = {6, 10, 30})
            void whenClientIsExperianAndRequestedLoanTermIsGreaterThan5ThenLeaveOriginalValue(int requestedLoanTerm) {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("experian");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(requestedLoanTerm);
            }
        }

        @Nested
        class ClearScore {

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            void whenClientIsClearScoreAndRequestedLoanTermIsLessThan3ThenReturnDeclinedResponse(int requestedLoanTerm) {
                // Given
                var declinedDecisionResponse = QuickQuoteResponse.builder()
                        .status("Declined")
                        .offers(null)
                        .build();

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);

                when(tokenService.retrieveClientId()).thenReturn("clearscore");

                // When
                var decisionResponse = filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                assertThat(decisionResponse).isEqualTo(declinedDecisionResponse);
                verify(selectionRepository, never()).filter(any(FilterQuickQuoteApplicationRequest.class));
                verify(middlewareRepository, never()).createQuickQuoteApplication(any(QuickQuoteRequest.class));
            }

            @ParameterizedTest
            @ValueSource(ints = {3, 4, 5})
            void whenClientIsClearScoreAndRequestedLoanTermIsBetween3And5ThenAdjustItTo5(int requestedLoanTerm) {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("clearscore");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            }

            @ParameterizedTest
            @ValueSource(ints = {6, 10, 30})
            void whenClientIsClearScoreAndRequestedLoanTermIsGreaterThan5ThenLeaveOriginalValue(int requestedLoanTerm) {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(requestedLoanTerm);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("clearscore");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(requestedLoanTerm);
            }
        }
    }

    @Nested
    class FilterResponseOffers extends MapperBase {

        @Test
        void whenClientIsClearScoreThenReturnLowestAprcHelocAndHomeownerLoanOffersOnly() {
            // Given
            var quickQuoteRequest = getQuickQuoteApplicationRequestDto();

            var decisionResponse = getFilteredQuickQuoteDecisionResponse();
            decisionResponse.setProducts(List.of(getProduct(HELOC, 10.0), getProduct(HELOC, 9.0),  getProduct(HOMEOWNER_LOAN, 8.0),  getProduct(HOMEOWNER_LOAN, 7.0)));

            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(getPartner());
            when(tokenService.retrieveClientId()).thenReturn("clearscore");

            // When
            var response = filterApplicationService.filter(quickQuoteRequest);

            // Then
            assertThat(response.getOffers())
                    .hasSize(2);
        }

        @Test
        void whenClientIsClearScoreAndThereAreTwoHelocAndHomeownerLoanOffersWithTheSameLowestAprcThenReturnOnlyTheFirstEachOne() {
            // Given
            var quickQuoteRequest = getQuickQuoteApplicationRequestDto();

            var decisionResponse = getFilteredQuickQuoteDecisionResponse();
            decisionResponse.setProducts(List.of(getProduct(HELOC, 10.0), getProduct(HELOC, 10.0), getProduct(HOMEOWNER_LOAN, 8.0), getProduct(HOMEOWNER_LOAN, 8.0)));

            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(getPartner());
            when(tokenService.retrieveClientId()).thenReturn("clearscore");

            // When
            var response = filterApplicationService.filter(quickQuoteRequest);

            // Then
            assertThat(response.getOffers())
                    .hasSize(2);
        }

        @Test
        void whenClientIsNotClearScoreThenReturnAllAcceptedOffers() {
            // Given
            var quickQuoteRequest = getQuickQuoteApplicationRequestDto();

            var decisionResponse = getFilteredQuickQuoteDecisionResponse();
            decisionResponse.setProducts(List.of(getProduct(HELOC, 10.0), getProduct(HELOC, 9.0), getProduct(HOMEOWNER_LOAN, 8.0), getProduct(HOMEOWNER_LOAN, 7.0)));

            when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
            when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
            when(eligibilityRepository.getEligibility(any(QuickQuoteApplicationRequest.class), anyList())).thenReturn(getEligibilityResponse());
            when(partnerService.getPartnerFromToken()).thenReturn(getPartner());
            when(tokenService.retrieveClientId()).thenReturn("other-client");

            // When
            var response = filterApplicationService.filter(quickQuoteRequest);

            // Then
            assertThat(response.getOffers())
                    .hasSize(4);
        }

        private Product getProduct(String family, double aprc) {
            var product = getProduct();
            product.setFamily(family);
            product.getOffer().setAprc(aprc);
            return product;
        }
        private ProductOfferDto getProductOfferDto(String family, double aprc) {
            var product = getProductOfferDto();
            product.setFamily(family);
            product.setAprc(aprc);
            return product;
        }
    }
}
