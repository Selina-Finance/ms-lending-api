package com.selina.lending.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.LendingService;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

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
        when(lendingService.getApplication(APPLICATION_ID)).thenReturn(Optional.of(mwApplicationDecisionResponse));

        //When
        lendingController.getApplication(APPLICATION_ID);

        //Then
        verify(lendingService, times(1)).getApplication(APPLICATION_ID);
    }

    @Test
    void createDipApplication() {
        //Given
        when(lendingService.createDipApplication(dipApplicationRequest)).thenReturn(mwApplicationResponse);

        //When
        lendingController.createDipApplication(dipApplicationRequest);

        //Then
        verify(lendingService, times(1)).createDipApplication(dipApplicationRequest);
    }

    @Test
    void updateDipApplication() {
        //Given
        var id = UUID.randomUUID().toString();
        var applicationType = "DIP";
        var mwApplicationResponse = ApplicationResponse.builder().applicationId(id).applicationType(applicationType).build();
        when(lendingService.updateDipApplication(APPLICATION_ID, dipApplicationRequest)).thenReturn(mwApplicationResponse);

        //When
        var appResponse = lendingController.updateDipApplication(APPLICATION_ID, dipApplicationRequest);

        //Then
        assertThat(Objects.requireNonNull(appResponse.getBody()).getRequestType(), equalTo(applicationType));
        assertThat(appResponse.getBody().getApplicationId(), equalTo(id));
        verify(lendingService, times(1)).updateDipApplication(APPLICATION_ID, dipApplicationRequest);
    }
}