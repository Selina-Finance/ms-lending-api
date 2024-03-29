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
import com.selina.lending.api.mapper.MapperBase;
import com.selina.lending.api.mapper.dipcc.DIPCCApplicationRequestMapper;
import com.selina.lending.exception.RemoteResourceProblemException;
import com.selina.lending.service.CreateApplicationService;
import com.selina.lending.service.RetrieveApplicationService;
import com.selina.lending.service.UpdateApplicationService;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
class DIPControllerCircuitBreakerTest extends MapperBase {

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
    void shouldReturnBadGatewayWhenGetDipApplicationHasMiddlewareProblem() throws Exception {
        //Given
        var dipId = UUID.randomUUID().toString();

        when(retrieveApplicationService.getApplicationByExternalApplicationId(dipId)).thenThrow(
                new RemoteResourceProblemException());

        //When
        mockMvc.perform(get("/application/" + dipId))
                //Then
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturnBadGatewayWhenCreateDipCCApplicationHasMiddlewareProblem() throws Exception {
        //Given
        var requestDto = getDIPCCApplicationRequestDto();

        when(createApplicationService.createDipCCApplication(
                DIPCCApplicationRequestMapper.INSTANCE.mapToApplicationRequest(requestDto))).thenThrow(
                new RemoteResourceProblemException());

        //When
        mockMvc.perform(post("/application/dipcc")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturnBadGatewayWhenUpdateDipCCApplicationHasMiddlewareProblem() throws Exception {
        //Given
        var dipId = UUID.randomUUID().toString();
        var requestDto = getDIPCCApplicationRequestDto();
        String jsonRequestDto = objectMapper.writeValueAsString(requestDto);

        doThrow(new RemoteResourceProblemException()).when(updateApplicationService).updateDipCCApplication(dipId,
                DIPCCApplicationRequestMapper.INSTANCE.mapToApplicationRequest(requestDto));

        //When
        mockMvc.perform(put("/application/" + dipId + "/dipcc")
                        .content(jsonRequestDto)
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturnNotFoundWhenDipApplicationDoesNotExistInMiddleware() throws Exception {
        //Given
        var dipId = UUID.randomUUID().toString();
        var request = Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate());
        var exception = new FeignException.NotFound("Not Found", request, "not found".getBytes(), null);

        when(retrieveApplicationService.getApplicationByExternalApplicationId(dipId)).thenThrow(exception);

        //When
        mockMvc.perform(get("/application/" + dipId))
                //Then
                .andExpect(status().isNotFound());
    }
}