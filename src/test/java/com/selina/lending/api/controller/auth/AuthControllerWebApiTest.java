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

package com.selina.lending.api.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.api.controller.AuthController;
import com.selina.lending.api.dto.auth.request.Credentials;
import com.selina.lending.api.dto.auth.response.TokenResponse;
import com.selina.lending.internal.service.AuthService;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static feign.Request.HttpMethod.GET;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(value = AuthController.class)
class AuthControllerWebApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void shouldReturn200OKWhenTokenWasBuiltSuccessfully() throws Exception {
        //Given
        var credentials = new Credentials("broker", "super-secret");
        var response = new TokenResponse("theTokenValue", 60);
        when(authService.getTokenByCredentials(credentials)).thenReturn(response);

        //When
        mockMvc.perform(
                        post("/auth/token")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(credentials))
                                .contentType(APPLICATION_JSON)
                )
                //Then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.accessToken").value(response.accessToken()))
                .andExpect(jsonPath("$.expiresIn").value(response.expiresIn()));
    }

    @Test
    void shouldReturn502BadGatewayWhenCreateTokenFailed() throws Exception {
        //Given
        var credentials = new Credentials("broker", "super-secret");

        var request = Request.create(GET, "/url", new HashMap<>(), null, new RequestTemplate());
        var exception = new FeignException.InternalServerError("Internal error", request, "the error msg".getBytes(), null);
        when(authService.getTokenByCredentials(credentials)).thenThrow(exception);

        //When
        mockMvc.perform(
                        post("/auth/token")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(credentials))
                                .contentType(APPLICATION_JSON)
                )
                //Then
                .andExpect(status().isBadGateway())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }
}