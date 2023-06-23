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

package com.selina.lending.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.internal.dto.LoanInformationDto;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.CreateApplicationService;
import com.selina.lending.internal.service.FilterApplicationService;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
class QuickQuoteControllerValidationTest extends MapperBase {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilterApplicationService filterApplicationService;

    @MockBean
    private CreateApplicationService createApplicationService;

    @MockBean
    private TokenService tokenService;

    @Nested
    class QuickQuoteApplication {

        @Test
        void whenCreateApplicationWithInvalidLoanAmountAndTermThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.setLoanInformation(LoanInformationDto.builder()
                    .numberOfApplicants(1)
                    .requestedLoanAmount(1000)
                    .requestedLoanTerm(2)
                    .loanPurpose(LOAN_PURPOSE)
                    .build());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(2)))
                    .andExpect(jsonPath("$.violations[0].field").value("loanInformation.requestedLoanAmount"))
                    .andExpect(jsonPath("$.violations[0].message").value("must be between 10000 and 1000000"))
                    .andExpect(jsonPath("$.violations[1].field").value("loanInformation.requestedLoanTerm"))
                    .andExpect(jsonPath("$.violations[1].message").value("must be between 5 and 30"));

        }

        @Test
        void whenCreateApplicationWithoutApplicantPhoneNumberThenReturnOkResponse() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().get(0).setMobileNumber(null);

            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON));

        }

        @Test
        void whenCreateApplicationWithoutPropertyDetailsEstimatedValueThenReturnOkResponse() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getPropertyDetails().setEstimatedValue(null);

            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON));

        }

        @Test
        void whenCreateApplicationWithoutApplicantsThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.setApplicants(null);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("must not be null"));

        }

        @Test
        void whenCreateApplicationWithApplicantsListEmptyThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().clear();
            request.getLoanInformation().setNumberOfApplicants(1);

            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(2)))
                    .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("applicants is required, min = 1, max = 2"))
                    .andExpect(jsonPath("$.violations[1].field").value("loanInformation.numberOfApplicants"))
                    .andExpect(jsonPath("$.violations[1].message").value("should be equal to applicants size"));
        }


        @Test
        void whenCreateApplicationWithNullApplicantsThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().add(null);
            request.getLoanInformation().setNumberOfApplicants(2);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("applicants[1]"))
                    .andExpect(jsonPath("$.violations[0].message").value("must not be null"));
        }

        @Test
        void whenCreateApplicationWithoutApplicantPrimaryApplicantThenReturnOkResponse() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().get(0).setPrimaryApplicant(null);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON));

        }

        @Test
        void whenCreateApplicationWithOneApplicantPrimaryApplicantThenReturnOkResponse() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().get(0).setPrimaryApplicant(true);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON));

        }

        @Test
        void whenCreateApplicationWithOneApplicantPrimaryApplicantFalseThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().get(0).setPrimaryApplicant(false);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("must have one primary applicant"));

        }

        @Test
        void whenCreateApplicationWithOneApplicantPrimaryApplicantTrueAndOneApplicantPrimaryApplicantFalseThenReturnOkResponse() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().add(getQuickQuoteApplicantDto());
            request.getApplicants().get(0).setPrimaryApplicant(true);
            request.getApplicants().get(1).setPrimaryApplicant(false);
            request.getLoanInformation().setNumberOfApplicants(2);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON));
        }

        @Test
        void whenCreateApplicationWithOneApplicantPrimaryApplicantTrueAndOneApplicantPrimaryApplicantNullThenReturnOkResponse() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().add(getQuickQuoteApplicantDto());
            request.getApplicants().get(0).setPrimaryApplicant(true);
            request.getApplicants().get(1).setPrimaryApplicant(null);
            request.getLoanInformation().setNumberOfApplicants(2);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON));
        }

        @Test
        void whenCreateApplicationWithTwoApplicantPrimaryApplicantNullThenReturnOkResponse() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().add(getQuickQuoteApplicantDto());
            request.getApplicants().get(0).setPrimaryApplicant(null);
            request.getApplicants().get(1).setPrimaryApplicant(null);
            request.getLoanInformation().setNumberOfApplicants(2);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON));
        }

        @Test
        void whenCreateApplicationWithTwoApplicantPrimaryApplicantTrueThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().add(getQuickQuoteApplicantDto());
            request.getApplicants().get(0).setPrimaryApplicant(true);
            request.getApplicants().get(1).setPrimaryApplicant(true);
            request.getLoanInformation().setNumberOfApplicants(2);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("must have one primary applicant"));
        }

        @Test
        void whenCreateApplicationWithOneApplicantPrimaryApplicantNullAndOneApplicantPrimaryApplicantFalseThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().add(getQuickQuoteApplicantDto());
            request.getApplicants().get(0).setPrimaryApplicant(false);
            request.getApplicants().get(1).setPrimaryApplicant(null);
            request.getLoanInformation().setNumberOfApplicants(2);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("must have one primary applicant"));
        }

        @Test
        void whenCreateApplicationWithTwoApplicantPrimaryApplicantFalseThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().add(getQuickQuoteApplicantDto());
            request.getApplicants().get(0).setPrimaryApplicant(false);
            request.getApplicants().get(1).setPrimaryApplicant(false);
            request.getLoanInformation().setNumberOfApplicants(2);
            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("must have one primary applicant"));
        }

        @Test
        void whenCreateQQApplicationWithOneApplicantAndLoanInformationNumberOfApplicantsIsOneThenReturnSuccess() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();

            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isOk());
        }

        @Test
        void whenCreateQQApplicationWithOneApplicantAndLoanInformationNumberOfApplicantsIsTwoThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getLoanInformation().setNumberOfApplicants(2);

            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("loanInformation.numberOfApplicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("should be equal to applicants size"));
        }

        @Test
        void whenCreateQQApplicationWithTwoApplicantAndLoanInformationNumberOfApplicantsIsOneThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteApplicationRequestDto();
            request.getApplicants().add(getQuickQuoteApplicantDto());

            when(filterApplicationService.filter(request)).thenReturn(getFilteredQuickQuoteDecisionResponse());

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("loanInformation.numberOfApplicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("should be equal to applicants size"));
        }

    }

    @Nested
    class QuickQuoteCFApplication {

        @Test
        void whenCreateApplicationWithoutApplicantPhoneNumberThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteCFApplicationRequestDto();
            request.getApplicants().get(0).setMobileNumber(null);

            when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(getQuickQuoteCFResponse());

            //When
            mockMvc.perform(post("/application/quickquotecf").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("applicants[0].mobileNumber"))
                    .andExpect(jsonPath("$.violations[0].message").value("must not be blank"));

        }

        @Test
        void whenCreateApplicationWithoutPropertyDetailsEstimatedValueThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteCFApplicationRequestDto();
            request.getPropertyDetails().setEstimatedValue(null);

            when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(getQuickQuoteCFResponse());

            //When
            mockMvc.perform(post("/application/quickquotecf").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("propertyDetails.estimatedValue"))
                    .andExpect(jsonPath("$.violations[0].message").value("must not be null"));

        }

        @Test
        void whenCreateApplicationWithoutPropertyDetailsBuildingNameAndBuildingNumberThenReturnBadRequest() throws Exception {
            //Given
            var request = getQuickQuoteCFApplicationRequestDto();
            var propertyDetails = request.getPropertyDetails();
            propertyDetails.setBuildingName("");
            propertyDetails.setBuildingNumber(null);

            when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(getQuickQuoteCFResponse());

            //When
            mockMvc.perform(post("/application/quickquotecf").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("propertyDetails"))
                    .andExpect(jsonPath("$.violations[0].message").value("At least one of these fields must be specified: [buildingName, buildingNumber]"));

        }

        @Test
        void shouldCreateApplicationWhenAtLeastPropertyDetailsBuildingNameIsSpecified() throws Exception {
            //Given
            var request = getQuickQuoteCFApplicationRequestDto();
            var propertyDetails = request.getPropertyDetails();
            propertyDetails.setBuildingNumber(null);

            when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(getQuickQuoteCFResponse());

            //When
            mockMvc.perform(post("/application/quickquotecf").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        }

        @Test
        void shouldCreateApplicationWhenAtLeastPropertyDetailsBuildingNumberIsSpecified() throws Exception {
            //Given
            var request = getQuickQuoteCFApplicationRequestDto();
            var propertyDetails = request.getPropertyDetails();
            propertyDetails.setBuildingName(null);

            when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(getQuickQuoteCFResponse());

            //When
            mockMvc.perform(post("/application/quickquotecf").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        }

        @Test
        void whenCreateApplicationWithBelowMinimumEstimatedPropertyValueThenBadRequest() throws Exception {
            var request = getQuickQuoteApplicationRequestDto();
            request.getPropertyDetails().setEstimatedValue(49_999.0);

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("propertyDetails.estimatedValue"))
                    .andExpect(jsonPath("$.violations[0].message").value("must be between 50000 and 99999999"));
        }

        @Test
        void whenCreateApplicationWithAboveMaximumEstimatedPropertyValueThenBadRequest() throws Exception {
            var request = getQuickQuoteApplicationRequestDto();
            request.getPropertyDetails().setEstimatedValue(100_000_000.01);

            //When
            mockMvc.perform(post("/application/quickquote").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("propertyDetails.estimatedValue"))
                    .andExpect(jsonPath("$.violations[0].message").value("must be between 50000 and 99999999"));
        }

        @Test
        void whenCreateQQCFApplicationWithOneApplicantAndLoanInformationNumberOfApplicantsIsOneThenSuccess() throws Exception {
            var request = getQuickQuoteCFApplicationRequestDto();
            when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(getQuickQuoteCFResponse());

            //When
            mockMvc.perform(post("/application/quickquotecf").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isOk());
        }

        @Test
        void whenCreateQQCFApplicationWithTwoApplicantAndLoanInformationNumberOfApplicantsIsTwoThenSuccess() throws Exception {
            var request = getQuickQuoteCFApplicationRequestDto();
            request.setApplicants(List.of(getQuickQuoteCFApplicantDto(), getQuickQuoteCFApplicantDto()));
            request.getLoanInformation().setNumberOfApplicants(2);
            when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(getQuickQuoteCFResponse());

            //When
            mockMvc.perform(post("/application/quickquotecf").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isOk());
        }

        @Test
        void whenCreateQQCFApplicationWithOneApplicantAndLoanInformationNumberOfApplicantsIsTwoThenBadRequest() throws Exception {
            var request = getQuickQuoteCFApplicationRequestDto();
            request.getLoanInformation().setNumberOfApplicants(2);
            when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(getQuickQuoteCFResponse());

            //When
            mockMvc.perform(post("/application/quickquotecf").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("loanInformation.numberOfApplicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("should be equal to applicants size"));
        }

        @Test
        void whenCreateQQCFApplicationWithTwoApplicantAndLoanInformationNumberOfApplicantsIsOneThenBadRequest() throws Exception {
            var request = getQuickQuoteCFApplicationRequestDto();
            request.setApplicants(List.of(getQuickQuoteCFApplicantDto(), getQuickQuoteCFApplicantDto()));
            when(createApplicationService.createQuickQuoteCFApplication(any(QuickQuoteCFRequest.class))).thenReturn(getQuickQuoteCFResponse());

            //When
            mockMvc.perform(post("/application/quickquotecf").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    //Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("loanInformation.numberOfApplicants"))
                    .andExpect(jsonPath("$.violations[0].message").value("should be equal to applicants size"));
        }
    }
}

