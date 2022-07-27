package com.selina.lending.internal.repository;

import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

public interface MiddlewareRepository {
    ApplicationResponse getApplicationById(String id);
}
