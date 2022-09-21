package com.selina.lending.api.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.mapper.DIPApplicationRequestMapper;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.CreateApplicationService;
import com.selina.lending.internal.service.RetrieveApplicationService;
import com.selina.lending.internal.service.UpdateApplicationService;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;

@WithMockUser
@WebMvcTest(value = LendingController.class)
class LendingControllerCircuitBreakerTest extends MapperBase {

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
    void shouldReturnBadGatewayWhenCreateDipApplicationHasMiddlewareProblem() throws Exception {
        //Given
        var requestDto = getDIPApplicationRequestDto();

        when(createApplicationService.createDipApplication(
                DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(requestDto))).thenThrow(
                new RemoteResourceProblemException());

        //When
        mockMvc.perform(post("/application/dip").with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturnBadGatewayWhenUpdateDipApplicationHasMiddlewareProblem() throws Exception {
        //Given
        var dipId = UUID.randomUUID().toString();
        var requestDto = getDIPApplicationRequestDto();
        String jsonRequestDto = objectMapper.writeValueAsString(requestDto);

        doThrow(new RemoteResourceProblemException()).when(updateApplicationService).updateDipApplication(dipId,
                DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(requestDto));

        //When
        mockMvc.perform(put("/application/" + dipId + "/dip").with(csrf())
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