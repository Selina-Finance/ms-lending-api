package com.selina.lending.internal.service;

import org.springframework.stereotype.Service;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.application.dto.ApplicationResponse;

@Service
public class LendingServiceImpl implements LendingService {
    @Override
    public ApplicationResponse getApplication(String id) {
        return null;
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
