package com.selina.lending.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.IntegrationTest;
import com.selina.lending.internal.dto.AdvancedLoanInformationDto;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.LendingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@IntegrationTest
public class LendingControllerValidationTest extends MapperBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LendingService lendingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createDipApplicationSuccess() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();

//        when(lendingService.createDipApplication(dipApplicationRequest)).thenReturn(response);

        //When
        mockMvc.perform(post("/application/dip").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    public void createDipApplicationWithEmptyDIPApplicationRequest() throws Exception {
        //Given
        var dipApplicationRequest = DIPApplicationRequest.builder().build();

        //When
        mockMvc.perform(post("/application/dip").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations", hasSize(5)))
                .andExpect(jsonPath("$.violations[0].field").value("applicants"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[1].field").value("loanInformation"))
                .andExpect(jsonPath("$.violations[1].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[2].field").value("productCode"))
                .andExpect(jsonPath("$.violations[2].message").value("must not be blank"))
                .andExpect(jsonPath("$.violations[3].field").value("propertyDetails"))
                .andExpect(jsonPath("$.violations[3].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[4].field").value("source"))
                .andExpect(jsonPath("$.violations[4].message").value("must not be blank"));
    }

    @Test
    public void createDipApplicationWithMissingApplicantsRequest() throws Exception {
        //Given
        var dipApplicationRequest = DIPApplicationRequest.builder()
                .requestType(DIP_APPLICATION_TYPE)
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(getAdvancedLoanInformationDto())
                .propertyDetails(getDIPPropertyDetailsDto())
                .productCode(PRODUCT_CODE)
                .source(SOURCE)
                .build();

        //When
        mockMvc.perform(post("/application/dip").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
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
    public void updateDipApplicationWithMissingMandatoryLoanInformation() throws Exception {
        //Given
        var dipApplicationRequest = DIPApplicationRequest.builder()
                .requestType(DIP_APPLICATION_TYPE)
                .externalApplicationId(EXTERNAL_APPLICATION_ID)
                .applicants(List.of(getDIPApplicantDto()))
                .expenditure(List.of(getExpenditureDto()))
                .loanInformation(AdvancedLoanInformationDto.builder().loanPurpose("invalid loanPurpose").facilities(List.of(getFacilityDto())).build())
                .propertyDetails(getDIPPropertyDetailsDto())
                .productCode(PRODUCT_CODE)
                .source(SOURCE)
                .build();

        //When
        mockMvc.perform(put("/application/123/dip").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
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
    public void updateDipApplicationSuccess() throws Exception {
        //Given
        var dipApplicationRequest = getDIPApplicationRequestDto();

        //When
        mockMvc.perform(put("/application/123/dip").with(csrf()).content(objectMapper.writeValueAsString(dipApplicationRequest))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk());
    }
}