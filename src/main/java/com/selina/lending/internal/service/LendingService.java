package com.selina.lending.internal.service;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

public interface LendingService {
    ApplicationResponse getApplication(String id);
    ApplicationResponse updateDipApplication(DIPApplicationRequest dipApplicationRequest);
    ApplicationResponse createDipApplication(DIPApplicationRequest dipApplicationRequest);
}
