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
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.mapper.MapperBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
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

    @Test
    void createQuickQuoteWithInvalidLoanAmountAndTerm() throws Exception {
        //Given
        QuickQuoteApplicationRequest request = getQuickQuoteApplicationRequestDto();
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
}
