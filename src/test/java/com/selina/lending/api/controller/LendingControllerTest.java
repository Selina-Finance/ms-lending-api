package com.selina.lending.api.controller;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.selina.lending.IntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.LendingService;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser
@AutoConfigureMockMvc
@IntegrationTest
public class LendingControllerTest {

    private static final String APPLICATION_ID = UUID.randomUUID().toString();
    @MockBean
    private LendingService lendingService;

    @MockBean
    private ApplicationDecisionResponse mwApplicationDecisionResponse;
    @MockBean
    private ApplicationResponse mwApplicationResponse;

    @MockBean
    private DIPApplicationRequest dipApplicationRequest;

    private LendingController lendingController;

    @BeforeEach
    public void setUp() {
        lendingController = new LendingController(lendingService);
    }

    @Test
    public void getApplication() {
        //Given
        when(lendingService.getApplication(eq(APPLICATION_ID))).thenReturn(Optional.of(mwApplicationDecisionResponse));

        //When
        lendingController.getApplication(APPLICATION_ID);

        //Then
        verify(lendingService, times(1)).getApplication(eq(APPLICATION_ID));
    }

    @Test
    public void createDipApplication() {
        //Given
        when(lendingService.createDipApplication(eq(dipApplicationRequest))).thenReturn(mwApplicationResponse);

        //When
        lendingController.createDipApplication(dipApplicationRequest);

        //Then
        verify(lendingService, times(1)).createDipApplication(eq(dipApplicationRequest));
    }

    @Test
    public void updateDipApplication() {
        //Given
        doNothing().when(lendingService).updateDipApplication(eq(APPLICATION_ID), eq(dipApplicationRequest));

        //When
        lendingController.updateDipApplication(APPLICATION_ID, dipApplicationRequest);

        //Then
        verify(lendingService, times(1)).updateDipApplication(eq(APPLICATION_ID), eq(dipApplicationRequest));
    }
}