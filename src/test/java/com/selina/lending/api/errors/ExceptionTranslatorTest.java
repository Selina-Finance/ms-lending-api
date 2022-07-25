package com.selina.lending.api.errors;

import com.selina.lending.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void testMissingServletRequestPartException() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/missing-servlet-request-part"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Required request part 'part' is not present"));
    }

    @Test
    void testMissingServletRequestParameterException() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/missing-servlet-request-parameter"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Required request parameter 'param' for method parameter type String is not present"));
    }

    @Test
    void testMethodNotSupported() throws Exception {
        mockMvc
                .perform(post("/api/exception-translator-test/missing-servlet-request-parameter"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Method Not Allowed"))
                .andExpect(jsonPath("$.detail").value("Request method 'POST' not supported"));
    }

    @Test
    void testExceptionWithResponseStatus() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/response-status"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("test response status"));
    }

    @Test
    void testInternalServerError() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/internal-server-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Internal Server Error"));
    }
}