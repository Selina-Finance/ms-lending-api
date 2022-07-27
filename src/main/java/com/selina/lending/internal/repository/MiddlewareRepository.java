package com.selina.lending.internal.repository;

import com.selina.lending.internal.service.application.domain.ApplicationResponse;

public interface MiddlewareRepository {
    ApplicationResponse getApplicationById(String id);
}
