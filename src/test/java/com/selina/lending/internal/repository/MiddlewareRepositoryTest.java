package com.selina.lending.internal.repository;


import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MiddlewareRepositoryTest {

    @Mock
    private MiddlewareApi middlewareApi;

    private MiddlewareRepository middlewareRepository;

    @BeforeEach
    public void setUp() {
        middlewareRepository = new MiddlewareRepositoryImpl(middlewareApi);
    }

    @Test
    public void shouldCallHttpClientWhenGetApplicationByIdInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        var apiResponse = new ApplicationResponse();

        when(middlewareApi.getApplicationById(id)).thenReturn(apiResponse);

        // When
        var result = middlewareRepository.getApplicationById(id);

        // Then
        assertThat(result).isEqualTo(apiResponse);
        verify(middlewareApi, times(1)).getApplicationById(eq(id));
    }
}