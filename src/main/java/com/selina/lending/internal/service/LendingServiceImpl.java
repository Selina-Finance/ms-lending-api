package com.selina.lending.internal.service;

import com.selina.lending.internal.repository.MiddlewareRepository;
import org.springframework.stereotype.Service;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

@Service
public class LendingServiceImpl implements LendingService {

    private final MiddlewareRepository middlewareRepository;

    public LendingServiceImpl(MiddlewareRepository middlewareRepository) {
        this.middlewareRepository = middlewareRepository;
    }

    @Override
    public ApplicationResponse getApplication(String id) {

        return middlewareRepository.getApplicationById(id);
    }

    @Override
    public ApplicationResponse updateDipApplication(DIPApplicationRequest dipApplicationRequest) {
        return null;
    }

    @Override
    public ApplicationResponse createDipApplication(DIPApplicationRequest dipApplicationRequest) {
        return null;
    }
}
