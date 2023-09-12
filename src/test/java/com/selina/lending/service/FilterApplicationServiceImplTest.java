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

import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.dto.qq.request.QuickQuoteFeesDto;
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.api.mapper.qq.middleware.MiddlewareQuickQuoteApplicationRequestMapper;
import com.selina.lending.httpclient.middleware.dto.common.Fees;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import com.selina.lending.httpclient.selection.dto.request.FilterQuickQuoteApplicationRequest;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.repository.MiddlewareRepository;
import com.selina.lending.repository.SelectionRepository;
import com.selina.lending.service.quickquote.ArrangementFeeSelinaService;
import com.selina.lending.service.quickquote.PartnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterApplicationServiceImplTest extends MapperBase {

    @Mock
    private SelectionRepository selectionRepository;

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

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private FilterApplicationServiceImpl filterApplicationService;

    @Test
    void shouldFilterQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequestWithCorrectDefaultValues() {
        // Given
        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(QuickQuoteApplicationRequest.class), any(), any())).thenReturn(quickQuoteRequest);
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        //When
        var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

        //Then
        verify(selectionRepository, times(1)).filter(selectionRequestCaptor.capture());
        verify(middlewareRepository, times(1)).createQuickQuoteApplication(quickQuoteRequest);
        verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();

        assertThat(response).isEqualTo(decisionResponse);

        var requestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(requestFees.getIsAddArrangementFeeSelinaToLoan()).isFalse();
        assertThat(requestFees.getIsAddProductFeesToFacility()).isFalse();
    }

    @Test
    void shouldFilterQuickQuoteApplicationAndSendMiddlewareCreateApplicationRequestWithoutDefaultValuesIfValuesPresent() {
        // Given
        var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

        var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                .decision("Accepted")
                .products(List.of(getProduct()))
                .build();

        var qqFees = QuickQuoteFeesDto.builder()
                .isAddArrangementFeeSelinaToLoan(true)
                .isAddProductFeesToFacility(true)
                .build();

        var qqApplicationRequest = QuickQuoteApplicationRequest.builder()
                .applicants(List.of())
                .loanInformation(getQQLoanInformationDto())
                .fees(qqFees)
                .build();

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(middlewareQuickQuoteApplicationRequestMapper
                .mapToQuickQuoteRequest(any(QuickQuoteApplicationRequest.class), any(), any())).thenReturn(quickQuoteRequest);
        when(partnerService.getPartnerFromToken()).thenReturn(null);

        //When
        var response = filterApplicationService.filter(qqApplicationRequest);

        //Then
        verify(selectionRepository, times(1)).filter(selectionRequestCaptor.capture());
        verify(middlewareRepository, times(1)).createQuickQuoteApplication(quickQuoteRequest);
        verify(arrangementFeeSelinaService, times(1)).getFeesFromToken();

        assertThat(response).isEqualTo(decisionResponse);

        var requestFees = selectionRequestCaptor.getValue().getApplication().getFees();
        assertThat(requestFees.getIsAddArrangementFeeSelinaToLoan()).isEqualTo(qqFees.getIsAddArrangementFeeSelinaToLoan());
        assertThat(requestFees.getIsAddProductFeesToFacility()).isEqualTo(qqFees.getIsAddProductFeesToFacility());
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

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
        when(partnerService.getPartnerFromToken()).thenReturn(getPartner());

        //When
        var response = filterApplicationService.filter(quickQuoteRequest);

        //Then
        verify(selectionRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
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
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
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
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
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
        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
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

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
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

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
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

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
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

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
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

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

        //When
        var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

        //Then
        verify(selectionRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
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

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

        //When
        var response = filterApplicationService.filter(getQuickQuoteApplicationRequestDto());

        //Then
        verify(selectionRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
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

        when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
        when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);
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

    @Nested
    class AlternativeOffer {

        @Test
        void whenRequestedLoanTermIsLessThan5ThenReturnDeclinedResponse() {
            // Given
            var declinedDecisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                    .decision("Declined")
                    .products(null)
                    .build();

            var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
            quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(2);

            when(tokenService.retrieveClientId()).thenReturn("some-aggregator");

            // When
            var decisionResponse = filterApplicationService.filter(quickQuoteApplicationRequest);

            // Then
            assertThat(decisionResponse).isEqualTo(declinedDecisionResponse);
            verify(selectionRepository, never()).filter(any(FilterQuickQuoteApplicationRequest.class));
            verify(middlewareRepository, never()).createQuickQuoteApplication(any(QuickQuoteRequest.class));
        }

        @Nested
        class Monevo {
            @Test
            void whenClientIsMonevoAndRequestedLoanTermIsLessThan5YearsThenAdjustItTo5() {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(1);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("monevo");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            }

            @Test
            void whenClientIsMonevoAndRequestedLoanTermIsGreaterThanOrEqualTo5YearsThenLeaveOriginalValue() {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(7);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("monevo");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(7);
            }
        }

        @Nested
        class ClearScore {
            @Test
            void whenClientIsClearScoreAndRequestedLoanTermIs4ThenAdjustItTo5() {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(4);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("clearscore");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            }

            @Test
            void whenClientIsClearScoreAndRequestedLoanTermIs3ThenAdjustItTo5() {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(3);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("clearscore");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(5);
            }

            @Test
            void whenClientIsClearScoreAndRequestedLoanTermIsLessThan3ThenReturnDeclinedResponse() {
                // Given
                var declinedDecisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(2);

                when(tokenService.retrieveClientId()).thenReturn("clearscore");

                // When
                var decisionResponse = filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                assertThat(decisionResponse).isEqualTo(declinedDecisionResponse);
                verify(selectionRepository, never()).filter(any(FilterQuickQuoteApplicationRequest.class));
                verify(middlewareRepository, never()).createQuickQuoteApplication(any(QuickQuoteRequest.class));
            }

            @Test
            void whenClientIsClearScoreAndRequestedLoanTermIsGreaterThanOrEqualTo5YearsThenLeaveOriginalValue() {
                // Given
                var decisionResponse = FilteredQuickQuoteDecisionResponse.builder()
                        .decision("Declined")
                        .products(null)
                        .build();

                when(arrangementFeeSelinaService.getFeesFromToken()).thenReturn(Fees.builder().build());
                when(selectionRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(decisionResponse);

                var quickQuoteApplicationRequest = getQuickQuoteApplicationRequestDto();
                quickQuoteApplicationRequest.getLoanInformation().setRequestedLoanTerm(7);

                var selectionRequestCaptor = ArgumentCaptor.forClass(FilterQuickQuoteApplicationRequest.class);

                when(tokenService.retrieveClientId()).thenReturn("clearscore");

                // When
                filterApplicationService.filter(quickQuoteApplicationRequest);

                // Then
                verify(selectionRepository).filter(selectionRequestCaptor.capture());
                assertThat(selectionRequestCaptor.getValue().getApplication().getLoanInformation().getRequestedLoanTerm()).isEqualTo(7);
            }
        }
    }
}
