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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.IntegrationTest;
import com.selina.lending.internal.dto.AdvancedLoanInformationDto;
import com.selina.lending.internal.dto.DIPCCApplicationRequest;
import com.selina.lending.internal.dto.EmploymentDto;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.CreateApplicationService;
import com.selina.lending.internal.service.RetrieveApplicationService;
import com.selina.lending.internal.service.UpdateApplicationService;

@WithMockUser
@AutoConfigureMockMvc
@IntegrationTest
class DIPControllerValidationTest extends MapperBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveApplicationService retrieveApplicationService;

    @MockBean
    private UpdateApplicationService updateApplicationService;

    @MockBean
    private CreateApplicationService createApplicationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createDipCCApplicationSuccess() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();

        //When
        mockMvc.perform(post("/application/dipcc").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    void createDipCCApplicationWithEmptyDIPApplicationRequest() throws Exception {
        //Given
        var dipApplicationRequest = DIPCCApplicationRequest.builder().build();

        //When
        mockMvc.perform(post("/application/dipcc").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(4)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[1].field").value("externalApplicationId"))
                .andExpect(jsonPath("$.violations[1].message").value("must not be blank"))
                .andExpect(jsonPath("$.violations[2].field").value("loanInformation"))
                .andExpect(jsonPath("$.violations[2].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[3].field").value("propertyDetails"))
                .andExpect(jsonPath("$.violations[3].message").value("must not be null"));
    }

    @Test
    void createDipCCApplicationWithMissingApplicantsRequest() throws Exception {
        //Given
        var dipApplicationRequest = DIPCCApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPCCPropertyDetailsDto())
                .build();

        //When
        mockMvc.perform(post("/application/dipcc").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
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
    void updateDipCCApplicationWithMissingMandatoryLoanInformation() throws Exception {
        //Given
        var dipApplicationRequest = DIPCCApplicationRequest.builder()
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .applicants(List.of(getDIPApplicantDto()))
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(AdvancedLoanInformationDto.builder().loanPurpose("invalid loanPurpose").facilities(List.of(getFacilityDto())).build())
                .propertyDetails(getDIPCCPropertyDetailsDto())
                .build();

        //When
        mockMvc.perform(put("/application/123/dipcc").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
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
    void updateDipCCApplicationSuccess() throws Exception {
        //Given
        var dipApplicationRequest = getDIPCCApplicationRequestDto();

        //When
        mockMvc.perform(put("/application/123/dipcc").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isNoContent());
    }

    @Test
    void createDipCCApplicationWithInvalidDateFormat() throws Exception {
        //Given
        var employment = EmploymentDto.builder()
                .employmentStatus(EMPLOYED_STATUS)
                .contractStartDate("invalid")
                .contractEndDate("21-01-2019")
                .whenWasCompanyIncorporated("2019/01/21")
                .partnershipFormedDate("Monday 21st January 2019")
                .whenDidYouBeginTrading("2019-01-21 12:00")
                .startDate("2019-01-21") //valid
                .build();

        var applicant = getDIPApplicantDto();
        applicant.setEmployment(employment);

        var dipApplicationRequest = DIPCCApplicationRequest.builder()
                .applicants(List.of(applicant))
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPCCPropertyDetailsDto())
                .build();

        //When
        mockMvc.perform(post("/application/dipcc").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(5)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants[0].employment.contractEndDate"))
                .andExpect(jsonPath("$.violations[0].message").value("must match yyyy-MM-dd format"))
                .andExpect(jsonPath("$.violations[1].field").value("applicants[0].employment.contractStartDate"))
                .andExpect(jsonPath("$.violations[1].message").value("must match yyyy-MM-dd format"))
                .andExpect(jsonPath("$.violations[2].field").value("applicants[0].employment.partnershipFormedDate"))
                .andExpect(jsonPath("$.violations[2].message").value("must match yyyy-MM-dd format"))
                .andExpect(jsonPath("$.violations[3].field").value("applicants[0].employment.whenDidYouBeginTrading"))
                .andExpect(jsonPath("$.violations[3].message").value("must match yyyy-MM-dd format"))
                .andExpect(jsonPath("$.violations[4].field").value("applicants[0].employment.whenWasCompanyIncorporated"))
                .andExpect(jsonPath("$.violations[4].message").value("must match yyyy-MM-dd format"));
    }
}