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
import com.jayway.jsonpath.JsonPath;
import com.selina.lending.api.dto.common.EmploymentDto;
import com.selina.lending.api.dto.common.PriorChargesDto;
import com.selina.lending.api.dto.dip.request.AdvancedLoanInformationDto;
import com.selina.lending.api.dto.dipcc.request.DIPCCApplicationRequest;
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.service.CreateApplicationService;
import com.selina.lending.service.RetrieveApplicationService;
import com.selina.lending.service.UpdateApplicationService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO delete
@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
class DIPControllerValidationTest extends MapperBase {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveApplicationService retrieveApplicationService;

    @MockBean
    private UpdateApplicationService updateApplicationService;

    @MockBean
    private CreateApplicationService createApplicationService;

    @Test
    void whenRequestIsNotAuthorizedThenReturnUnauthorized() throws Exception {
        // Given
        SecurityContextHolder.getContext().setAuthentication(null);
        var dipApplicationRequest = getDIPCCApplicationRequestDto();

        // When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Unauthorized"))
                .andExpect(jsonPath("$.detail").value("Full authentication is required to access this resource"));
    }

    @Test
    void shouldCreateDipCCApplicationSuccessfully() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithEmptyDIPApplicationRequest() throws Exception {
        //Given
        var dipApplicationRequest = DIPCCApplicationRequest.builder().build();

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(6)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("must have one primary applicant"))
                .andExpect(jsonPath("$.violations[1].field").value("applicants"))
                .andExpect(jsonPath("$.violations[1].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[2].field").value("externalApplicationId"))
                .andExpect(jsonPath("$.violations[2].message").value("must not be blank"))
                .andExpect(jsonPath("$.violations[3].field").value("loanInformation"))
                .andExpect(jsonPath("$.violations[3].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[4].field").value("propertyDetails"))
                .andExpect(jsonPath("$.violations[4].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[5].field").value("sourceClientId"))
                .andExpect(jsonPath("$.violations[5].message").value("must not be null"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithMissingApplicantsRequest() throws Exception {
        //Given
        var dipApplicationRequest = DIPCCApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .sourceClientId(SOURCE_CLIENT_ID)
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPCCPropertyDetailsDto())
                .build();

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(2)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("must have one primary applicant"))
                .andExpect(jsonPath("$.violations[1].field").value("applicants"))
                .andExpect(jsonPath("$.violations[1].message").value("must not be null"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithApplicantMobileNumberIsInvalid() throws Exception {
        //Given
        var request = getDIPCCApplicationRequestDto();
        request.getApplicants().get(0).setMobileNumber("012345AB90");

        //When
        mockMvc.perform(post("/application/dipcc")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].mobileNumber"))
                .andExpect(jsonPath("$.violations[0].message").value("must be a valid UK phone number"));
    }


    @Test
    void shouldGiveValidationErrorWhenUpdateDipCCApplicationWithMissingMandatoryLoanInformation() throws Exception {
        //Given
        var dipApplicationRequest = DIPCCApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .sourceClientId(SOURCE_CLIENT_ID)
                .applicants(List.of(getDIPApplicantDto()))
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(AdvancedLoanInformationDto.builder().loanPurpose("invalid loanPurpose").facilities(List.of(getFacilityDto())).build())
                .propertyDetails(getDIPCCPropertyDetailsDto())
                .build();

        //When
        mockMvc.perform(put("/application/123/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(4)))
                .andExpect(jsonPath("$.violations[0].field").value("loanInformation.loanPurpose"))
                .andExpect(jsonPath("$.violations[0].message").value("value is not valid"))
                .andExpect(jsonPath("$.violations[1].field").value("loanInformation.numberOfApplicants"))
                .andExpect(jsonPath("$.violations[1].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[2].field").value("loanInformation.requestedLoanAmount"))
                .andExpect(jsonPath("$.violations[2].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[3].field").value("loanInformation.requestedLoanTerm"))
                .andExpect(jsonPath("$.violations[3].message").value("must not be null"));
    }

    @Test
    void shouldUpdateDipCCApplicationSuccessfully() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();

        //When
        mockMvc.perform(put("/application/123/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithInvalidDateFormat() throws Exception {
        //Given
        var employment = EmploymentDto.builder()
                .employmentStatus(EMPLOYED_STATUS)
                .contractStartDate("invalid")
                .contractEndDate("21-01-2019")
                .whenWasCompanyIncorporated("2019/01/21")
                .partnershipFormedDate("Monday 21st January 2019")
                .whenDidYouBeginTrading("2019-01-21")  //valid
                .build();

        var applicant = getDIPApplicantDto();
        applicant.setEmployment(employment);

        var dipApplicationRequest = DIPCCApplicationRequest.builder()
                .applicants(List.of(applicant))
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .sourceClientId(SOURCE_CLIENT_ID)
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPCCPropertyDetailsDto())
                .build();

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(4)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].employment.contractEndDate"))
                .andExpect(jsonPath("$.violations[0].message").value("must match yyyy-MM-dd format"))
                .andExpect(jsonPath("$.violations[1].field").value("applicants[0].employment.contractStartDate"))
                .andExpect(jsonPath("$.violations[1].message").value("must match yyyy-MM-dd format"))
                .andExpect(jsonPath("$.violations[2].field").value("applicants[0].employment.partnershipFormedDate"))
                .andExpect(jsonPath("$.violations[2].message").value("must match yyyy-MM-dd format"))
                .andExpect(jsonPath("$.violations[3].field").value("applicants[0].employment.whenWasCompanyIncorporated"))
                .andExpect(jsonPath("$.violations[3].message").value("must match yyyy-MM-dd format"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithInvalidTitle() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).setTitle("invalid title");
        dipApplicationRequest.getApplicants().get(0).getPreviousNames().get(0).setTitle("invalid title");

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(2)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].previousNames[0].title"))
                .andExpect(jsonPath("$.violations[0].message").value("value is not valid"))
                .andExpect(jsonPath("$.violations[1].field").value("applicants[0].title"))
                .andExpect(jsonPath("$.violations[1].message").value("value is not valid"));
    }

    @Test
    void shouldGetApplicationWithDateTimeInExpectedFormat() throws Exception {
        //Given
        var expectedDatePattern = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{6}$";
        var response = getApplicationDecisionResponse();
        when(retrieveApplicationService.getApplicationByExternalApplicationId("1")).thenReturn(Optional.of(response));

        //When
        MvcResult result = mockMvc.perform(get("/application/{externalApplicationId}", "1")
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk())
                .andReturn();

        String createdDate = JsonPath.read(result.getResponse().getContentAsString(), "$.createdDate");
        String modifiedDate = JsonPath.read(result.getResponse().getContentAsString(), "$.modifiedDate");

        assertTrue(modifiedDate.matches(expectedDatePattern));
        assertTrue(createdDate.matches(expectedDatePattern));
    }


    @ParameterizedTest
    @ValueSource(strings = {"a_b@a.co.uk", "b+c@testing.tech", "foo.bar@gmail.com", "test-email12@123.com"})
    void shouldCreateDIPApplicationWhenEmailAddressIsValid(String email) throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).setEmailAddress(email);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a@a", "b+c@test", "foo", "test-email@123."})
    void shouldGiveValidationErrorWhenCreateDIPApplicationWithInvalidEmail(String email) throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).setEmailAddress(email);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].emailAddress"))
                .andExpect(jsonPath("$.violations[0].message").value("emailAddress is not valid"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWithApplicantMobileNumberIsInvalid() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).setMobileNumber("+AS12LDS12314");

        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].mobileNumber"))
                .andExpect(jsonPath("$.violations[0].message").value("must be a valid UK phone number"));
    }

    @Test
    void whenCreateDipApplicationWithApplicantsSizeIsOneAndLoanInformationNumberOfApplicantsIsTwoThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(2);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
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
    void whenCreateDipApplicationWithApplicantsSizeIsTwoAndLoanInformationNumberOfApplicantsIsOneThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(1);
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);
        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        secondApplicant.setApplicant2LivesWithApplicant1For3Years(true);
        secondApplicant.setApplicant2LivesWithApplicant1(true);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
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
    void whenCreateDipApplicationWithApplicantsSizeIsTwoAndLoanInformationNumberOfApplicantsIsTwoThenReturnSuccess() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(2);
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);
        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        secondApplicant.setApplicant2LivesWithApplicant1For3Years(true);
        secondApplicant.setApplicant2LivesWithApplicant1(true);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateDipApplicationWithNegativePriorChargesBalanceOutstandingThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getPropertyDetails().getPriorCharges().setBalanceOutstanding(-100.0);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(2)))
                .andExpect(jsonPath("$.violations[0].field").value("propertyDetails.priorCharges.balanceConsolidated"))
                .andExpect(jsonPath("$.violations[0].message").value("The 'balanceConsolidated' must be less than or equal to the 'balanceOutstanding'"))
                .andExpect(jsonPath("$.violations[1].field").value("propertyDetails.priorCharges.balanceOutstanding"))
                .andExpect(jsonPath("$.violations[1].message").value("must be greater than or equal to 0"));
    }

    @Test
    void whenCreateDipApplicationWithEmployerNameLengthLessThan3ThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getEmployment().setEmployerName("ab");

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].employment.employerName"))
                .andExpect(jsonPath("$.violations[0].message").value("size must be between 3 and 100"));
    }

    @Test
    void whenCreateDipApplicationWithNotSpecifiedEmployerNameThenReturnOk() throws Exception {
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getEmployment().setEmployerName(null);

        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateDipApplicationWithNegativePriorChargesBalanceConsolidatedThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getPropertyDetails().getPriorCharges().setBalanceConsolidated(-100.0);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("propertyDetails.priorCharges.balanceConsolidated"))
                .andExpect(jsonPath("$.violations[0].message").value("must be greater than or equal to 0"));
    }

    @Test
    void whenCreateDipApplicationWithNegativePriorChargesMonthlyPaymentThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getPropertyDetails().getPriorCharges().setMonthlyPayment(-100.0);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("propertyDetails.priorCharges.monthlyPayment"))
                .andExpect(jsonPath("$.violations[0].message").value("must be greater than or equal to 0"));
    }

    @Test
    void whenCreateDipApplicationWithNegativePriorChargesOtherDebtPaymentsThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getPropertyDetails().getPriorCharges().setOtherDebtPayments(-100.0);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("propertyDetails.priorCharges.otherDebtPayments"))
                .andExpect(jsonPath("$.violations[0].message").value("must be greater than or equal to 0"));
    }

    @Test
    void whenCreateDipApplicationWithPriorChargesBalanceConsolidatedGreaterThanBalanceOutstandingThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        PriorChargesDto priorCharges = dipApplicationRequest.getPropertyDetails().getPriorCharges();
        priorCharges.setBalanceOutstanding(1000.0);
        priorCharges.setBalanceConsolidated(2000.0);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("propertyDetails.priorCharges.balanceConsolidated"))
                .andExpect(jsonPath("$.violations[0].message").value("The 'balanceConsolidated' must be less than or equal to the 'balanceOutstanding'"));
    }

    @Test
    void whenCreateDipApplicationWithPriorChargesBalanceConsolidatedEqualToBalanceOutstandingThenReturnOk() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        PriorChargesDto priorCharges = dipApplicationRequest.getPropertyDetails().getPriorCharges();
        priorCharges.setBalanceOutstanding(1000.0);
        priorCharges.setBalanceConsolidated(1000.0);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateDipApplicationWithoutSpecifyingPriorChargesBalanceOutstandingThenReturnOk() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getPropertyDetails().getPriorCharges().setBalanceOutstanding(null);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateDipApplicationWithoutSpecifyingPriorChargesBalanceConsolidatedThenReturnOk() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getPropertyDetails().getPriorCharges().setBalanceConsolidated(null);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateDipApplicationWithApplicantsSizeIsOneAndLoanInformationNumberOfApplicantsIsOneThenReturnSuccess() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }


    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithoutSpecifiedApplicantAddressBuildingNameAndBuildingNumber() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var addressDto = dipApplicationRequest.getApplicants().get(0).getAddresses().get(0);
        addressDto.setBuildingName("");
        addressDto.setBuildingNumber(null);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].addresses[0]"))
                .andExpect(jsonPath("$.violations[0].message").value("At least one of these fields must be specified: [buildingName, buildingNumber]"));
    }

    @Test
    void shouldCreateDipCCApplicationWhenAtLeastApplicantAddressBuildingNameIsSpecified() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var addressDto = dipApplicationRequest.getApplicants().get(0).getAddresses().get(0);
        addressDto.setBuildingNumber(null);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateDipCCApplicationWhenAtLeastApplicantAddressBuildingNumberIsSpecified() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var addressDto = dipApplicationRequest.getApplicants().get(0).getAddresses().get(0);
        addressDto.setBuildingName(null);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithoutSpecifiedPropertyDetailsBuildingNameAndBuildingNumber() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var propertyDetails = dipApplicationRequest.getPropertyDetails();
        propertyDetails.setBuildingName("");
        propertyDetails.setBuildingNumber(null);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
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
    void shouldCreateDipCCApplicationWhenAtLeastPropertyDetailsBuildingNameIsSpecified() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var propertyDetails = dipApplicationRequest.getPropertyDetails();
        propertyDetails.setBuildingNumber(null);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateDipCCApplicationWhenAtLeastPropertyDetailsBuildingNumberIsSpecified() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var propertyDetails = dipApplicationRequest.getPropertyDetails();
        propertyDetails.setBuildingName(null);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithoutSpecifyingSecondApplicantLivingStatusWithFirstApplicant() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);
        secondApplicant.setApplicant2LivesWithApplicant1For3Years(false);

        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(2);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("The field 'applicant2LivesWithApplicant1' is required for the second applicant"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithoutSpecifyingSecondApplicantLivingStatusWithFirstApplicantFor3Years() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);
        secondApplicant.setApplicant2LivesWithApplicant1(false);

        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(2);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("The field 'applicant2LivesWithApplicant1For3Years' is required for the second applicant"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithoutSpecifyingApplicantIncomeAmount() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getIncome().getIncome().get(0).setAmount(null);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income.income[0].amount"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithoutSpecifyingApplicantIncomeType() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getIncome().getIncome().get(0).setType(null);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income.income[0].type"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithoutSpecifyingIncorrectApplicantIncomeType() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getIncome().getIncome().get(0).setType("Unsupported value");

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income.income[0].type"))
                .andExpect(jsonPath("$.violations[0].message").value("value is not valid"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipCCApplicationWithoutSpecifyingSecondApplicantAnyLivingStatusWithFirstApplicant() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);

        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(2);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(2)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("The field 'applicant2LivesWithApplicant1' is required for the second applicant"))
                .andExpect(jsonPath("$.violations[1].field").value("applicants"))
                .andExpect(jsonPath("$.violations[1].message").value("The field 'applicant2LivesWithApplicant1For3Years' is required for the second applicant"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWithoutSpecifyingSecondApplicantLivingStatusWithFirstApplicant() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);
        secondApplicant.setApplicant2LivesWithApplicant1For3Years(false);

        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(2);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("The field 'applicant2LivesWithApplicant1' is required for the second applicant"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWithoutSpecifyingSecondApplicantLivingStatusWithFirstApplicantFor3Years() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);
        secondApplicant.setApplicant2LivesWithApplicant1(false);

        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(2);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("The field 'applicant2LivesWithApplicant1For3Years' is required for the second applicant"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWithoutSpecifyingApplicantIncomeAmount() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getIncome().getIncome().get(0).setAmount(null);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income.income[0].amount"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWithoutSpecifyingApplicantIncomeType() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getIncome().getIncome().get(0).setType(null);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income.income[0].type"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWithoutSpecifyingIncorrectApplicantIncomeType() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getIncome().getIncome().get(0).setType("Unsupported value");

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income.income[0].type"))
                .andExpect(jsonPath("$.violations[0].message").value("value is not valid"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWithoutSpecifyingSecondApplicantAnyLivingStatusWithFirstApplicant() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);

        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(2);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(2)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("The field 'applicant2LivesWithApplicant1' is required for the second applicant"))
                .andExpect(jsonPath("$.violations[1].field").value("applicants"))
                .andExpect(jsonPath("$.violations[1].message").value("The field 'applicant2LivesWithApplicant1For3Years' is required for the second applicant"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWithoutSpecifyingAllowedExpectsFutureIncomeDecreaseReason() throws Exception {
        // Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getIncome().setExpectsFutureIncomeDecrease(true);
        dipApplicationRequest.getApplicants().get(0).getIncome().setExpectsFutureIncomeDecreaseReason("Unsupported value");

        // When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income.expectsFutureIncomeDecreaseReason"))
                .andExpect(jsonPath("$.violations[0].message").value("value is not valid"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationHasExpectsFutureIncomeDecreaseTrueButNoReason() throws Exception {
        // Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getIncome().setExpectsFutureIncomeDecrease(true);
        dipApplicationRequest.getApplicants().get(0).getIncome().setExpectsFutureIncomeDecreaseReason(null);

        // When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income.expectsFutureIncomeDecreaseReason"))
                .andExpect(jsonPath("$.violations[0].message").value("This field is required"));
    }

    @Test
    void shouldGiveNoValidationErrorWhenCreateDipApplicationSpecifyAllowedExpectsFutureIncomeDecreaseFalseButReasonSupplied() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        dipApplicationRequest.getApplicants().get(0).getIncome().setExpectsFutureIncomeDecrease(false);
        dipApplicationRequest.getApplicants().get(0).getIncome().setExpectsFutureIncomeDecreaseReason("Redundancy");

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWithoutSpecifyingMainIncome() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        var applicant = dipApplicationRequest.getApplicants().get(0);
        applicant.setIncome(null);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"));
    }

    @Test
    void shouldGiveValidationErrorWhenCreateDipApplicationWhenSpecifyNullForIncomeItemsList() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        var applicant = dipApplicationRequest.getApplicants().get(0);
        applicant.getIncome().setIncome(null);

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].income.income"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"));
    }

    @Test
    void shouldCreateDipApplicationWithoutSpecifiedAnyApplicantsIncomeItems() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        var applicant = dipApplicationRequest.getApplicants().get(0);
        applicant.getIncome().setIncome(emptyList());

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateDipCCApplicationWithApplicantsSizeIsOneAndLoanInformationNumberOfApplicantsIsOneThenReturnSuccess() throws Exception {
        //Given
        var dipCCApplicationRequest = getDIPCCApplicationRequestDto();

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipCCApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateDipCCApplicationWithApplicantsSizeIsTwoAndLoanInformationNumberOfApplicantsIsTwoThenReturnSuccess() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);
        secondApplicant.setApplicant2LivesWithApplicant1(true);
        secondApplicant.setApplicant2LivesWithApplicant1For3Years(true);

        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(2);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateDipCCApplicationWithApplicantsSizeIsTwoAndLoanInformationNumberOfApplicantsIsOneThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();
        var firstApplicant = getDIPApplicantDto();
        var secondApplicant = getDIPApplicantDto();
        secondApplicant.setPrimaryApplicant(false);
        secondApplicant.setApplicant2LivesWithApplicant1(true);
        secondApplicant.setApplicant2LivesWithApplicant1For3Years(true);

        dipApplicationRequest.setApplicants(List.of(firstApplicant, secondApplicant));
        dipApplicationRequest.getLoanInformation().setNumberOfApplicants(1);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
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
    void whenCreateDipCCApplicationWithApplicantsSizeIsOneAndLoanInformationNumberOfApplicantsIsTwoThenReturnBadRequest() throws Exception {
        //Given
        var dipCCApplicationRequest = getDIPCCApplicationRequestDto();
        dipCCApplicationRequest.getLoanInformation().setNumberOfApplicants(2);

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipCCApplicationRequest))
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
    void whenCreateDipApplicationWithLoanInformationFacilitiesAllocationPurposeThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getLoanInformation().getFacilities().get(0).setAllocationPurpose("Incorrect value");

        //When
        mockMvc.perform(post("/application/dip").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("loanInformation.facilities[0].allocationPurpose"))
                .andExpect(jsonPath("$.violations[0].message").value("value is not valid"));
    }

    @Test
    void whenCreateDipCCApplicationWithLoanInformationFacilitiesAllocationPurposeThenReturnBadRequest() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();
        dipApplicationRequest.getLoanInformation().getFacilities().get(0).setAllocationPurpose("Incorrect value");

        //When
        mockMvc.perform(post("/application/dipcc").content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("loanInformation.facilities[0].allocationPurpose"))
                .andExpect(jsonPath("$.violations[0].message").value("value is not valid"));
    }

    @Nested
    class DIPExpendituresValidation {

        @Test
        void whenExpenditureAmountDeclaredIsNotSpecifiedThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPApplicationRequestDto();
            request.getExpenditure().get(0).setAmountDeclared(null);

            //When
            mockMvc.perform(post("/application/dip")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure[0].amountDeclared"))
                    .andExpect(jsonPath("$.violations[0].message").value("must not be null"));
        }

        @Test
        void whenExpenditureTypeIsNotSpecifiedThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPApplicationRequestDto();
            request.getExpenditure().get(0).setExpenditureType(null);

            //When
            mockMvc.perform(post("/application/dip")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure[0].expenditureType"))
                    .andExpect(jsonPath("$.violations[0].message").value("must not be blank"));
        }

        @Test
        void whenExpenditureTypeHasInvalidValueThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPApplicationRequestDto();
            request.getExpenditure().get(0).setExpenditureType("some unsupported value");

            //When
            mockMvc.perform(post("/application/dip")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure[0].expenditureType"))
                    .andExpect(jsonPath("$.violations[0].message").value("value is not valid"));
        }

        @Test
        void whenHaveTwoExpendituresOfTheSameTypeButWithDifferentFrequencyThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPApplicationRequestDto();
            var expenditure1 = getExpenditureDto();
            expenditure1.setFrequency("monthly");

            var expenditure2 = getExpenditureDto();
            expenditure2.setFrequency("daily");

            request.setExpenditure(List.of(expenditure1, expenditure2));

            //When
            mockMvc.perform(post("/application/dip")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure"))
                    .andExpect(jsonPath("$.violations[0].message").value("All expenditures of the same type must have the same frequency value"));
        }

        @Test
        void whenHaveTwoExpendituresOfTheSameTypeButOneOfThemHasNotSpecifiedFrequencyThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPApplicationRequestDto();
            var expenditure1 = getExpenditureDto();
            expenditure1.setFrequency("monthly");

            var expenditure2 = getExpenditureDto();
            expenditure2.setFrequency(null);

            request.setExpenditure(List.of(expenditure1, expenditure2));

            //When
            mockMvc.perform(post("/application/dip")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure"))
                    .andExpect(jsonPath("$.violations[0].message").value("All expenditures of the same type must have the same frequency value"));
        }

        @Test
        void whenHaveTwoExpendituresOfTheDifferentTypeAndDifferentFrequencyThenReturnOk() throws Exception {
            //Given
            var request = getDIPApplicationRequestDto();
            var expenditure1 = getExpenditureDto("Utilities");
            expenditure1.setFrequency("monthly");

            var expenditure2 = getExpenditureDto("Other");
            expenditure2.setFrequency("daily");

            request.setExpenditure(List.of(expenditure1, expenditure2));

            //When
            mockMvc.perform(post("/application/dip")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class DIPCCExpendituresValidation {

        @Test
        void whenExpenditureAmountDeclaredIsNotSpecifiedThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPCCApplicationRequestDto();
            request.getExpenditure().get(0).setAmountDeclared(null);

            //When
            mockMvc.perform(post("/application/dipcc")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure[0].amountDeclared"))
                    .andExpect(jsonPath("$.violations[0].message").value("must not be null"));
        }

        @Test
        void whenExpenditureTypeIsNotSpecifiedThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPCCApplicationRequestDto();
            request.getExpenditure().get(0).setExpenditureType(null);

            //When
            mockMvc.perform(post("/application/dipcc")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure[0].expenditureType"))
                    .andExpect(jsonPath("$.violations[0].message").value("must not be blank"));
        }

        @Test
        void whenExpenditureTypeHasInvalidValueThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPCCApplicationRequestDto();
            request.getExpenditure().get(0).setExpenditureType("some unsupported value");

            //When
            mockMvc.perform(post("/application/dipcc")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure[0].expenditureType"))
                    .andExpect(jsonPath("$.violations[0].message").value("value is not valid"));
        }

        @Test
        void whenHaveTwoExpendituresOfTheSameTypeButWithDifferentFrequencyThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPCCApplicationRequestDto();
            var expenditure1 = getExpenditureDto();
            expenditure1.setFrequency("monthly");

            var expenditure2 = getExpenditureDto();
            expenditure2.setFrequency("daily");

            request.setExpenditure(List.of(expenditure1, expenditure2));

            //When
            mockMvc.perform(post("/application/dipcc")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure"))
                    .andExpect(jsonPath("$.violations[0].message").value("All expenditures of the same type must have the same frequency value"));
        }

        @Test
        void whenHaveTwoExpendituresOfTheSameTypeButOneOfThemHasNotSpecifiedFrequencyThenReturnBadRequest() throws Exception {
            //Given
            var request = getDIPCCApplicationRequestDto();
            var expenditure1 = getExpenditureDto();
            expenditure1.setFrequency("monthly");

            var expenditure2 = getExpenditureDto();
            expenditure2.setFrequency(null);

            request.setExpenditure(List.of(expenditure1, expenditure2));

            //When
            mockMvc.perform(post("/application/dipcc")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("expenditure"))
                    .andExpect(jsonPath("$.violations[0].message").value("All expenditures of the same type must have the same frequency value"));
        }

        @Test
        void whenHaveTwoExpendituresOfTheDifferentTypeAndDifferentFrequencyThenReturnOk() throws Exception {
            //Given
            var request = getDIPCCApplicationRequestDto();
            var expenditure1 = getExpenditureDto("Utilities");
            expenditure1.setFrequency("monthly");

            var expenditure2 = getExpenditureDto("Other");
            expenditure2.setFrequency("daily");

            request.setExpenditure(List.of(expenditure1, expenditure2));

            //When
            mockMvc.perform(post("/application/dipcc")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON))
                    // Then
                    .andExpect(status().isOk());
        }
    }
}