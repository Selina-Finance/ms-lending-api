package com.selina.lending.internal.service;

import java.util.Optional;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

public interface LendingService {
    Optional<ApplicationResponse> getApplication(String id);
    ApplicationResponse updateDipApplication(DIPApplicationRequest dipApplicationRequest);
    ApplicationResponse createDipApplication(DIPApplicationRequest dipApplicationRequest);
}
