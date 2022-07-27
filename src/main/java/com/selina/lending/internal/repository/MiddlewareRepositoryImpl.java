package com.selina.lending.internal.repository;

import com.selina.lending.internal.api.MiddlewareApi;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import org.springframework.stereotype.Service;

@Service
public class MiddlewareRepositoryImpl implements MiddlewareRepository {
    private final MiddlewareApi middlewareApi;

    public MiddlewareRepositoryImpl(MiddlewareApi middlewareApi) {
        this.middlewareApi = middlewareApi;
    }

    @Override
    public ApplicationResponse getApplicationById(String id) {
        return middlewareApi.getApplicationById(id);
    }
}
