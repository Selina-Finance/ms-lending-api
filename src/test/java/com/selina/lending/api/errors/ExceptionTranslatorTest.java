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

package com.selina.lending.api.errors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
class ExceptionTranslatorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleInvalidInputException() throws Exception {
        // Given
        var invalidInputJson = "{,,][[";

        // When
        mockMvc.perform(
                        post("/api/exception-translator-test/method-argument")
                                .with(csrf())
                                .content(invalidInputJson)
                                .contentType(APPLICATION_JSON)
                )
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.detail").value("JSON parse error: Unexpected character (',' (code 44)): was expecting double-quote to start field name"));
    }

    @Test
    void handleConstraintViolation() throws Exception {
        // Given
        String emptyContent = "{}";

        // When
        mockMvc.perform(
                        post("/api/exception-translator-test/method-argument")
                                .with(csrf())
                                .content(emptyContent)
                                .contentType(APPLICATION_JSON)
                )

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.violations.[0].field").value("test"))
                .andExpect(jsonPath("$.violations.[0].message").value("must not be null"));
    }

    @Test
    void handleAccessDeniedException() throws Exception {
        // Given
        var expectedTitle = "Error processing request";
        var expectedDetail = "Some problem details that make sense";

        // When
        mockMvc.perform(get("/api/exception-translator-test/access-denied-exception"))

                // Then
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(expectedDetail));
    }

    @Test
    void handleRemoteResourceProblemException() throws Exception {
        // Given
        var expectedTitle = "Bad Gateway";
        var expectedDetail = "Received an invalid response from the upstream server";

        // When
        mockMvc.perform(get("/api/exception-translator-test/custom-remote-resource-problem-exception"))

                // Then
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(expectedDetail));
    }

    @Test
    void handleBadRequestException() throws Exception {
        // Given
        var expectedTitle = "Bad Request";
        var expectedDetail = "bad request";

        // When
        mockMvc.perform(get("/api/exception-translator-test/bad-request-exception"))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(expectedDetail));
    }

    @Test
    void handleMissingServletRequestPartException() throws Exception {
        // Given
        String expectedDetail = "Required request part 'part' is not present";

        // When
        mockMvc.perform(get("/api/exception-translator-test/missing-servlet-request-part"))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.detail").value(expectedDetail));
    }

    @Test
    void handleMissingServletRequestParameterException() throws Exception {
        // Given
        String expectedDetail = "Required request parameter 'param' for method parameter type String is not present";

        // When
        mockMvc.perform(get("/api/exception-translator-test/missing-servlet-request-parameter"))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.detail").value(expectedDetail));
    }

    @Test
    void handleMethodNotSupported() throws Exception {
        // Given
        String expectedDetail = "Request method 'POST' not supported";

        // When
        mockMvc.perform(
                        post("/api/exception-translator-test/missing-servlet-request-parameter")
                                .with(csrf())
                )

                // Then
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.title").value("Method Not Allowed"))
                .andExpect(jsonPath("$.detail").value(expectedDetail));
    }

    @Test
    void testAccessDenied() throws Exception {
        // Given
        String expectedValue = "test access denied!";

        // When
        mockMvc
                .perform(get("/api/exception-translator-test/access-denied"))
                // Then
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(expectedValue));

    }

    @Test
    void testUnauthorized() throws Exception {
        // Given
        String expectedMsg = "test authentication failed!";

        // When
        mockMvc
                .perform(get("/api/exception-translator-test/unauthorized"))
                // Then
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(expectedMsg));

    }

    @Test
    void handleExceptionWithResponseStatus() throws Exception {
        // Given
        String expectedTitle = "test response status";

        // When
        mockMvc.perform(get("/api/exception-translator-test/response-status"))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.title").value(expectedTitle));
    }

    @Test
    void handleInternalServerError() throws Exception {
        // Given
        String expectedTitle = "Internal Server Error";

        // When
        mockMvc.perform(get("/api/exception-translator-test/internal-server-error"))

                // Then
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.title").value(expectedTitle));
    }

    @Test
    void handleConflictException() throws Exception {
        //Given
        String expectedMsg = "conflict request";
        String expectedTitle = "Error processing request";

        //When
        mockMvc.perform(get("/api/exception-translator-test/conflict-exception"))

                //Then
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.detail").value(expectedMsg));
    }

    @Test
    void handleFeignBadRequestException() throws Exception {
        //Given
        String expectedMsg = "Sorry, unable to process your request";
        String expectedTitle = "Bad Gateway";

        //When
        mockMvc.perform(get("/api/exception-translator-test/feign-bad-request-exception"))

                //Then
                .andExpect(status().isBadGateway())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.detail").value(expectedMsg));
    }

    @Test
    void handleFeignInternalServerErrorException() throws Exception {
        //Given
        String expectedMsg = "Sorry, unable to process your request";
        String expectedTitle = "Bad Gateway";

        //When
        mockMvc.perform(get("/api/exception-translator-test/feign-internal-server-error-exception"))

                //Then
                .andExpect(status().isBadGateway())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.detail").value(expectedMsg));
    }

    @Test
    void handleFeignNotFoundRequestException() throws Exception {
        //Given
        String expectedTitle = "Not Found";
        String expectedMsg = "The entity does not exist";


        //When
        mockMvc.perform(get("/api/exception-translator-test/feign-not-found-exception"))

                //Then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.detail").value(expectedMsg));
    }

}