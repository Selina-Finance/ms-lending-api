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

package com.selina.lending.repository;

import com.selina.lending.httpclient.middleware.MiddlewareApi;
import com.selina.lending.httpclient.middleware.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import com.selina.lending.httpclient.middleware.dto.dip.response.Application;
import com.selina.lending.httpclient.middleware.dto.dip.response.ApplicationResponse;
import com.selina.lending.httpclient.middleware.dto.product.response.SelectProductResponse;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.response.QuickQuoteCFResponse;
import com.selina.lending.repository.circuitbreaker.RecordExceptionPredicate;
import com.selina.lending.service.enricher.MiddlewareRequestEnricher;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MiddlewareRepositoryTest {
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";

    @Mock
    private MiddlewareApi middlewareApi;

    @Mock
    private ApplicationRequest applicationRequest;

    @Mock
    private ApplicationResponse applicationResponse;

    @Mock
    private Application application;

    @Mock
    private QuickQuoteCFRequest quickQuoteCFRequest;

    @Mock
    private QuickQuoteCFResponse quickQuoteCFResponse;

    @Mock
    private QuickQuoteRequest quickQuoteRequest;

    private MiddlewareRepository middlewareRepository;

    @Mock
    private MiddlewareRequestEnricher middlewareRequestEnricher;

    @BeforeEach
    void setUp() {
        middlewareRepository = new MiddlewareRepositoryImpl(middlewareApi, middlewareRequestEnricher);
    }

    @Test
    void shouldCallHttpClientWhenGetApplicationByIdInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        var apiResponse = ApplicationDecisionResponse.builder().build();
        when(middlewareApi.getApplicationById(id)).thenReturn(apiResponse);

        // When
        var result = middlewareRepository.getApplicationById(id);

        // Then
        assertThat(result).isEqualTo(Optional.of(apiResponse));
        verify(middlewareApi, times(1)).getApplicationById(id);
    }

    @Test
    void shouldCallHttpClientWhenCreateDipCCApplicationInvoked() {
        // Given
        when(middlewareApi.createDipCCApplication(applicationRequest)).thenReturn(applicationResponse);
        when(applicationResponse.getApplication()).thenReturn(application);
        when(application.getExternalApplicationId()).thenReturn(EXTERNAL_APPLICATION_ID);

        // When
        var result = middlewareRepository.createDipCCApplication(applicationRequest);

        // Then
        assertThat(result).isEqualTo(applicationResponse);
        verify(middlewareRequestEnricher, times(1)).enrichCreateDipCCApplicationRequest(applicationRequest);
        verify(middlewareApi, times(1)).createDipCCApplication(applicationRequest);
    }

    @Test
    void shouldCallHttpClientWhenCreateDipApplicationInvoked() {
        // Given
        when(middlewareApi.createDipApplication(applicationRequest)).thenReturn(applicationResponse);
        when(applicationResponse.getApplication()).thenReturn(application);
        when(application.getExternalApplicationId()).thenReturn(EXTERNAL_APPLICATION_ID);

        // When
        var result = middlewareRepository.createDipApplication(applicationRequest);

        // Then
        assertThat(result).isEqualTo(applicationResponse);
        verify(middlewareRequestEnricher, times(1)).enrichCreateDipApplicationRequest(applicationRequest);
        verify(middlewareApi, times(1)).createDipApplication(applicationRequest);
    }

    @Test
    void shouldCallHttpClientWhenCreateQuickQuoteCFApplicationInvoked() {
        // Given
        when(middlewareApi.createQuickQuoteCFApplication(quickQuoteCFRequest)).thenReturn(quickQuoteCFResponse);
        when(quickQuoteCFResponse.getExternalApplicationId()).thenReturn(EXTERNAL_APPLICATION_ID);

        // When
        var result = middlewareRepository.createQuickQuoteCFApplication(quickQuoteCFRequest);

        // Then
        assertThat(result).isEqualTo(quickQuoteCFResponse);
        verify(middlewareRequestEnricher, times(1)).enrichCreateQuickQuoteCFRequest(quickQuoteCFRequest);
        verify(middlewareApi, times(1)).createQuickQuoteCFApplication(quickQuoteCFRequest);
    }

    @Test
    void shouldCallHttpClientWhenPatchDipApplicationInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        doNothing().when(middlewareApi).patchDipApplication(id, applicationRequest);

        // When
        middlewareRepository.patchDipApplication(id, applicationRequest);

        // Then
        verify(middlewareRequestEnricher, times(1)).enrichPatchApplicationRequest(applicationRequest);
        verify(middlewareApi, times(1)).patchDipApplication(id, applicationRequest);
    }

    @Test
    void shouldCallHttpClientWhenPatchDipCCApplicationInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        doNothing().when(middlewareApi).patchDipCCApplication(id, applicationRequest);

        // When
        middlewareRepository.patchDipCCApplication(id, applicationRequest);

        // Then
        verify(middlewareRequestEnricher, times(1)).enrichPatchApplicationRequest(applicationRequest);
        verify(middlewareApi, times(1)).patchDipCCApplication(id, applicationRequest);
    }

    @Test
    void shouldCallHttpClientWhenCheckAffordabilityInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        var apiResponse = ApplicationResponse.builder().build();
        when(middlewareApi.checkAffordability(id)).thenReturn(apiResponse);

        // When
        var result = middlewareRepository.checkAffordability(id);

        // Then
        assertThat(result).isEqualTo(apiResponse);
        verify(middlewareApi, times(1)).checkAffordability(id);
    }

    @Test
    void shouldThrowFeignServerExceptionWhenCheckAffordabilityThrowsInternalServerException() {
        //Given
        String errorMsg = "error";
        var id = UUID.randomUUID().toString();

        when(middlewareApi.checkAffordability(anyString())).thenThrow(
                new FeignException.FeignServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg, createRequest(),
                        errorMsg.getBytes(), null));

        //When
        var exception = assertThrows(FeignException.FeignServerException.class,
                () -> middlewareRepository.checkAffordability(id));

        //Then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
        verify(middlewareApi, times(1)).checkAffordability(id);
    }

    @Test
    void shouldThrowFeignServerExceptionWhenMiddlewareThrowsInternalServerException() {
        //Given
        String errorMsg = "error";
        var id = UUID.randomUUID().toString();

        //When
        when(middlewareApi.getApplicationById(id)).thenThrow(
                new FeignException.FeignServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg, createRequest(),
                        errorMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignServerException.class,
                () -> middlewareRepository.getApplicationById(id));

        //Then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
    }

    @Test
    void shouldThrowFeignClientExceptionWhenMiddlewareThrowsNotFoundException() {
        //Given
        String notFoundMsg = "not found";

        //When
        when(middlewareApi.createDipCCApplication(applicationRequest)).thenThrow(
                new FeignException.FeignClientException(HttpStatus.NOT_FOUND.value(), notFoundMsg, createRequest(),
                        notFoundMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignClientException.class,
                () -> middlewareRepository.createDipCCApplication(applicationRequest));

        //Then
        assertThat(exception.getMessage()).isEqualTo(notFoundMsg);
    }

    @Test
    void shouldCallHttpClientWhenSelectProductInvoked() {
        // Given
        var id = UUID.randomUUID().toString();
        var productCode = "PR01";
        var apiResponse = SelectProductResponse.builder().id("appId").message("success").build();
        when(middlewareApi.selectProduct(id, productCode)).thenReturn(apiResponse);

        // When
        var result = middlewareRepository.selectProduct(id, productCode);

        // Then
        assertThat(result.getId()).isEqualTo("appId");
        assertThat(result.getMessage()).isEqualTo("success");
        verify(middlewareApi, times(1)).selectProduct(id, productCode);
    }


    @Test
    void shouldThrowFeignClientExceptionWhenSelectProductThrowsNotFoundException() {
        //Given
        String notFoundMsg = "not found";
        var id = UUID.randomUUID().toString();
        var productCode = "PR01";

        //When
        when(middlewareApi.selectProduct(id, productCode)).thenThrow(
                new FeignException.FeignClientException(HttpStatus.NOT_FOUND.value(), notFoundMsg, createRequest(),
                        notFoundMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignClientException.class,
                () -> middlewareRepository.selectProduct(id, productCode));

        //Then
        assertThat(exception.getMessage()).isEqualTo(notFoundMsg);
        verify(middlewareApi, times(1)).selectProduct(id, productCode);
    }

    @Test
    void shouldThrowFeignServerExceptionWhenSelectProductThrowsInternalServerException() {
        //Given
        String errorMsg = "error";
        var id = UUID.randomUUID().toString();
        var productCode = "PR01";

        //When
        when(middlewareApi.selectProduct(id, productCode)).thenThrow(
                new FeignException.FeignServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg, createRequest(),
                        errorMsg.getBytes(), null));

        var exception = assertThrows(FeignException.FeignServerException.class,
                () -> middlewareRepository.selectProduct(id, productCode));

        //Then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
        verify(middlewareApi, times(1)).selectProduct(id, productCode);
    }

    @Test
    void shouldCallHttpClientWhenDownloadEsisDocByIdInvoked() {
        // Given
        var id = UUID.randomUUID().toString();

        var apiResponse = new ByteArrayResource(new byte[0]);
        when(middlewareApi.downloadEsisByAppId(any())).thenReturn(apiResponse);

        // When
        var result = middlewareRepository.downloadEsisDocByAppId(id);

        // Then
        assertThat(result).isNotNull();
        verify(middlewareApi, times(1)).downloadEsisByAppId(id);
    }

    @Test
    void shouldThrowFeignClientExceptionWhenDownloadEsisDocThrowsNotFoundException() {
        //Given
        String notFoundMsg = "not found";
        var id = UUID.randomUUID().toString();

        when(middlewareApi.downloadEsisByAppId(any())).thenThrow(
                new FeignException.FeignClientException(HttpStatus.NOT_FOUND.value(), notFoundMsg, createRequest(),
                        notFoundMsg.getBytes(), null));
        //When
        var exception = assertThrows(FeignException.FeignClientException.class,
                () -> middlewareRepository.downloadEsisDocByAppId(id));

        //Then
        assertThat(exception.getMessage()).isEqualTo(notFoundMsg);
        verify(middlewareApi, times(1)).downloadEsisByAppId(id);
    }

    @Test
    void shouldThrowFeignServerExceptionWhenDownloadEsisThrowsInternalServerException() {
        //Given
        String errorMsg = "error";
        var id = UUID.randomUUID().toString();

        when(middlewareApi.downloadEsisByAppId(any())).thenThrow(
                new FeignException.FeignServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg, createRequest(),
                        errorMsg.getBytes(), null));

        //When
        var exception = assertThrows(FeignException.FeignServerException.class,
                () -> middlewareRepository.downloadEsisDocByAppId(id));

        //Then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
        verify(middlewareApi, times(1)).downloadEsisByAppId(id);
    }

    @Test
    void shouldCallHttpClientWhenCreateQuickQuoteApplicationInvoked() {
        // Given

        // When
        middlewareRepository.createQuickQuoteApplication(quickQuoteRequest);

        // Then
        verify(middlewareApi, times(1)).createQuickQuoteApplication(quickQuoteRequest);
    }

    @Test
    void shouldThrowFeignServerExceptionWhenCreateQuickQuoteThrowsInternalServerException() {
        //Given
        String errorMsg = "error";
        var id = UUID.randomUUID().toString();

        doThrow(new FeignException.FeignServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg, createRequest(),
                errorMsg.getBytes(), null)).when(middlewareApi).createQuickQuoteApplication(quickQuoteRequest);
        //When
        var exception = assertThrows(FeignException.FeignServerException.class,
                () -> middlewareRepository.createQuickQuoteApplication(quickQuoteRequest));

        //Then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
        verify(middlewareApi, times(1)).createQuickQuoteApplication(quickQuoteRequest);
    }


    @Test
    void shouldOpenCircuitBreakerWhenFeignServerExceptionTriggersFallback() {
        //Given
        var id = UUID.randomUUID().toString();
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApi.getApplicationById(id)).thenThrow(
                new FeignException.InternalServerError("Internal Server Error", createRequest(), "error".getBytes(), null));

        var supplier = circuitBreaker.decorateSupplier(() -> middlewareRepository.getApplicationById(id));

        IntStream.range(0, 10).forEach(x -> {
            try {
                supplier.get();
            } catch (Exception ignore) {
            }
        });

        //Then
        var metrics = circuitBreaker.getMetrics();
        assertThat(metrics.getNumberOfFailedCalls()).isEqualTo(5);
        assertThat(metrics.getNumberOfNotPermittedCalls()).isEqualTo(5);

        verify(middlewareApi, times(5)).getApplicationById(id);
    }


    @Test
    void shouldOpenCircuitBreakerWhenRetryableExceptionTriggersFallback() {
        //Given
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApi.createDipCCApplication(applicationRequest)).thenThrow(new feign.RetryableException(-1, "", Request.HttpMethod.GET, new Date(), createRequest()));

        var supplier = circuitBreaker.decorateSupplier(() -> middlewareRepository.createDipCCApplication(applicationRequest));

        IntStream.range(0, 10).forEach(x -> {
            try {
                supplier.get();
            } catch (Exception ignore) {
            }
        });

        //Then
        var metrics = circuitBreaker.getMetrics();
        assertThat(metrics.getNumberOfFailedCalls()).isEqualTo(5);
        assertThat(metrics.getNumberOfNotPermittedCalls()).isEqualTo(5);

        verify(middlewareApi, times(5)).createDipCCApplication(applicationRequest);
    }

    @Test
    void shouldNotTriggerCircuitBreakerFallbackForIgnoredExceptions() {
        //Given
        var id = UUID.randomUUID().toString();
        var circuitBreaker = getCircuitBreaker();

        //When
        when(middlewareApi.getApplicationById(id)).thenThrow(
                new FeignException.NotFound("Not found", createRequest(), "not found".getBytes(), null));

        var supplier = circuitBreaker.decorateSupplier(() -> middlewareRepository.getApplicationById(id));

        IntStream.range(0, 10).forEach(x -> {
            try {
                supplier.get();
            } catch (Exception ignore) {
            }
        });

        //Then
        var metrics = circuitBreaker.getMetrics();
        assertThat(metrics.getNumberOfFailedCalls()).isZero();
        assertThat(metrics.getNumberOfNotPermittedCalls()).isZero();

        verify(middlewareApi, times(10)).getApplicationById(id);
    }

    private Request createRequest() {
        return Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate());
    }

    private CircuitBreaker getCircuitBreaker() {
        var config = CircuitBreakerConfig.custom()
                .failureRateThreshold(60)
                .recordException(new RecordExceptionPredicate())
                .slidingWindowSize(5)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .build();
        var registry = CircuitBreakerRegistry.of(config);
        return registry.circuitBreaker("mw-cb");
    }
}