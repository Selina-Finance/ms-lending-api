package com.selina.lending.internal.repository;

import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MiddlewareRepositoryImpl implements MiddlewareRepository {
    private final MiddlewareApi middlewareApi;

    public MiddlewareRepositoryImpl(MiddlewareApi middlewareApi) {
        this.middlewareApi = middlewareApi;
    }

    @CircuitBreaker(name = "middleware-api-cb", fallbackMethod = "middlewareApiFallback")
    @Override
    public ApplicationResponse getApplicationById(String id) {
        log.debug("Request to get application by id: {}", id);
        return middlewareApi.getApplicationById(id);
    }

    public ApplicationResponse middlewareApiFallback(Exception e) {
        log.debug("Remote service is unavailable. Returning fallback.");
        return new ApplicationResponse();
    }
}
