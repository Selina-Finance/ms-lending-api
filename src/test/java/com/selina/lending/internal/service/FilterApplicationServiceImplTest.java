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

package com.selina.lending.internal.service;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.mapper.quote.middleware.MiddlewareQuickQuoteApplicationRequestMapper;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.repository.SelectionServiceRepository;
import com.selina.lending.internal.service.application.domain.Fees;
import com.selina.lending.internal.service.application.domain.quote.middleware.QuickQuoteRequest;
import com.selina.lending.internal.service.application.domain.quote.selection.FilterQuickQuoteApplicationRequest;
import com.selina.lending.internal.service.application.domain.quote.selection.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.internal.service.quickquote.ArrangementFeeSelinaService;
import com.selina.lending.internal.service.quickquote.PartnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterApplicationServiceImplTest extends MapperBase {

    @Mock
    private SelectionServiceRepository selectionServiceRepository;

    @Mock
    private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

    @Mock
    private MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper;
    @Mock
    private QuickQuoteRequest quickQuoteRequest;

    @Mock
    private MiddlewareRepository middlewareRepository;

    @Mock
    private ArrangementFeeSelinaService arrangementFeeSelinaService;

    @Mock
    private PartnerService partnerService;

    @InjectMocks
    private FilterApplicationServiceImpl filterApplicationService;

    @Test
    void shouldFilterQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequest() {
        // Given
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(QuickQuoteApplicationRequest.class), any(), any())).thenReturn(quickQuoteRequest);
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        //When
        var response = filterApplicationService.filter(quickQuoteApplicationRequest);

        //Then
        verify(selectionServiceRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
        verify(middlewareRepository, times(1)).createQuickQuoteApplication(quickQuoteRequest);
        verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();
        assertThat(response).isEqualTo(decisionResponse);
    }

    @Test
    void shouldFilterQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequestWithPartnerAdded() {
        // Given
        var quickQuoteRequest = getQuickQuoteApplicationRequestDto();
        quickQuoteRequest.setPartner(null);

        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(partnerService.getPartnerFromToken()).thenReturn(getPartner());

        //When
        var response = filterApplicationService.filter(quickQuoteRequest);

        //Then
        verify(selectionServiceRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
        verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();
        verify(partnerService, times(1)).getPartnerFromToken();
        assertThat(quickQuoteRequest.getPartner().getSubUnitId()).isEqualTo(SUB_UNIT_ID);
        assertThat(quickQuoteRequest.getPartner().getCompanyId()).isEqualTo(COMPANY_ID);
        assertThat(response).isEqualTo(decisionResponse);
    }

    @Test
    void whenHaveOneApplicantWithPrimaryApplicantNullThenApplicantPrimaryApplicantIsTrue() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(null);
        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(), any(), any())).thenReturn(quickQuoteRequest);
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
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(true);
        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(), any(), any())).thenReturn(quickQuoteRequest);
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
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(true);
        quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(false);
        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(), any(), any())).thenReturn(quickQuoteRequest);
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
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(), any(), any())).thenReturn(quickQuoteRequest);
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
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(), any(), any())).thenReturn(quickQuoteRequest);
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
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(), any(), any())).thenReturn(quickQuoteRequest);
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
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        quickQuoteApplicationRequest.getApplicants().get(0).setPrimaryApplicant(null);
        quickQuoteApplicationRequest.getApplicants().get(1).setPrimaryApplicant(null);
        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(), any(), any())).thenReturn(quickQuoteRequest);
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

        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

        //When
        var response = filterApplicationService.filter(quickQuoteApplicationRequest);

        //Then
        verify(selectionServiceRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
        verify(middlewareRepository, times(0)).createQuickQuoteApplication(any(QuickQuoteRequest.class));
        assertThat(response).isEqualTo(decisionResponse);
    }

    @Test
    void whenDecisionResponseHasNullProductOffersThenDoNotSendMiddlewareRequest() {
        //Given
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Declined")
                .products(null)
                .build();

        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

        //When
        var response = filterApplicationService.filter(quickQuoteApplicationRequest);

        //Then
        verify(selectionServiceRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
        verify(middlewareRepository, times(0)).createQuickQuoteApplication(any(QuickQuoteRequest.class));
        assertThat(response).isEqualTo(decisionResponse);
    }

    @Test
    void shouldCreateApplicationRequestWithFeesIfProvidedButNoArrangementFeeSelinaFields() {
        // Given
        QuickQuoteApplicationRequest quickQuoteApplicationRequest = getQuickQuoteApplicationRequestWithFeesDto();
        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(), any(), any())).thenReturn(quickQuoteRequest);

        // When
        filterApplicationService.filter(quickQuoteApplicationRequest);

        // Then
        assertThat(quickQuoteApplicationRequest.getFees().getArrangementFee()).isEqualTo(1000.00);
        assertThat(quickQuoteApplicationRequest.getFees().getAdviceFee()).isEqualTo(599.00);
        assertTrue(quickQuoteApplicationRequest.getFees().getIsAddAdviceFeeToLoan());
    }

}
