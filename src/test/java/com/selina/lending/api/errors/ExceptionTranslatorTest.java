package com.selina.lending.api.errors;

import com.selina.lending.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class ExceptionTranslatorTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testMethodArgumentNotValid() throws Exception {
        mockMvc.perform(post("/api/exception-translator-test/method-argument").content("{}").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations.[0].field").value("test"))
                .andExpect(jsonPath("$.violations.[0].message").value("must not be null"));
    }
}