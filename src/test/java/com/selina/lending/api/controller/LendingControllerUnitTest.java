package com.selina.lending.api.controller;


import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.LendingService;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LendingControllerUnitTest {

    private static final String APPLICATION_ID = UUID.randomUUID().toString();

    @Mock
    private ApplicationDecisionResponse mwApplicationDecisionResponse;

    @Mock
    private ApplicationResponse mwApplicationResponse;

    @Mock
    private DIPApplicationRequest dipApplicationRequest;

    @Mock
    private LendingService lendingService;

    @InjectMocks
    private LendingController lendingController;

    @Test
    void getApplication() {
        //Given
        when(lendingService.getApplication(eq(APPLICATION_ID))).thenReturn(Optional.of(mwApplicationDecisionResponse));

        //When
        lendingController.getApplication(APPLICATION_ID);

        //Then
        verify(lendingService, times(1)).getApplication(eq(APPLICATION_ID));
    }

    @Test
    void createDipApplication() {
        //Given
        when(lendingService.createDipApplication(eq(dipApplicationRequest))).thenReturn(mwApplicationResponse);

        //When
        lendingController.createDipApplication(dipApplicationRequest);

        //Then
        verify(lendingService, times(1)).createDipApplication(eq(dipApplicationRequest));
    }

    @Test
    void updateDipApplication() {
        //Given
        doNothing().when(lendingService).updateDipApplication(eq(APPLICATION_ID), eq(dipApplicationRequest));

        //When
        lendingController.updateDipApplication(APPLICATION_ID, dipApplicationRequest);

        //Then
        verify(lendingService, times(1)).updateDipApplication(eq(APPLICATION_ID), eq(dipApplicationRequest));
    }
}