package com.selina.lending.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.LendingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MimeType;
import org.zalando.problem.Problem;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {LendingController.class})
public class LendingControllerErrorHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LendingService lendingService;

    @Test
    public void shouldReturnReadableErrorDescriptionOn4xxConstraintViolationException() throws Exception {
        //Given
        var invalidRequest = new DIPApplicationRequest();
        var expectedErrorResponse = """
                {
                    "type": "https://zalando.github.io/problem/constraint-violation",
                    "status": 400,
                    "violations": [
                        {
                            "field": "productCode",
                            "message": "productCode is required"
                        },
                        {
                            "field": "source",
                            "message": "source is required"
                        }
                    ],
                    "title": "Constraint Violation"
                }
                """;

        //When
        MvcResult result = mockMvc.perform(post("/application/dip")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();


        //Then
        String resultAsString = result.getResponse().getContentAsString();
        assertThat(resultAsString).isEqualTo(expectedErrorResponse);
    }
}