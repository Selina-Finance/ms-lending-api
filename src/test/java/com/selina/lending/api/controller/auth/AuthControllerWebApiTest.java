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
import com.selina.lending.config.security.SecurityConfig;
import com.selina.lending.internal.dto.auth.AuthTokenResponse;
import com.selina.lending.internal.dto.auth.CredentialsDto;
import com.selina.lending.internal.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest(value = AuthController.class)
class AuthControllerWebApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void shouldReturn200OKWhenTokenWasSuccessfullyBuilt() throws Exception {
        //Given
        var credentials = new CredentialsDto("broker", "super-secret");
        var expectedToken = new AuthTokenResponse("theTokenValue", 60);
        when(authService.getTokenByCredentials(credentials)).thenReturn(expectedToken);

        //When
        mockMvc.perform(
                        post("/lending/auth/token")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(credentials))
                                .contentType(APPLICATION_JSON)
                )
                //Then
                .andExpect(status().is2xxSuccessful());
    }
}