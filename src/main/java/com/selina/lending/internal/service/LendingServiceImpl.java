package com.selina.lending.internal.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.mapper.DIPApplicationRequestMapper;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

@Service
public class LendingServiceImpl implements LendingService {
    @Override
    public Optional<ApplicationResponse> getApplication(String id) {
        return null;
    }

    @Override
    public ApplicationResponse updateDipApplication(DIPApplicationRequest dipApplicationRequest) {
        ApplicationRequest applicationRequest = DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);

        return null;
    }

    @Override
    public ApplicationResponse createDipApplication(DIPApplicationRequest dipApplicationRequest) {
        ApplicationRequest applicationRequest = DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);
        return null;
    }
}
