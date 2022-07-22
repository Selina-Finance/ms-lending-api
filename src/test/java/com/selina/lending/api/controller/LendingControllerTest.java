package com.selina.lending.api.controller;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.LendingService;
import com.selina.lending.internal.service.application.dto.ApplicationResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest (controllers = LendingController.class)
public class LendingControllerTest {

    private static final String APPLICATION_ID = UUID.randomUUID().toString();
    @MockBean
    private LendingService lendingService;

    @MockBean
    private ApplicationResponse mwApplicationResponse;

    @MockBean
    private DIPApplicationRequest dipApplicationRequest;

    private LendingController lendingController;

    @BeforeAll
    public void setUp() {
        lendingController = new LendingController(lendingService);
    }

    @Test
    public void getApplication() {
        when(lendingService.getApplication(eq(APPLICATION_ID))).thenReturn(mwApplicationResponse);

        lendingController.getApplication(APPLICATION_ID);

        verify(lendingService, times(1)).getApplication(eq(APPLICATION_ID));
    }

    @Test
    public void createDipApplication() {
        when(lendingService.createDipApplication(eq(dipApplicationRequest))).thenReturn(mwApplicationResponse);

        lendingController.createDipApplication(dipApplicationRequest);

        verify(lendingService, times(1)).createDipApplication(eq(dipApplicationRequest));
    }

    @Test
    public void updateDipApplication() {
        when(lendingService.updateDipApplication(eq(dipApplicationRequest))).thenReturn(mwApplicationResponse);

        lendingController.updateDipApplication(APPLICATION_ID, dipApplicationRequest);

        verify(lendingService, times(1)).updateDipApplication(eq(dipApplicationRequest));
    }
}