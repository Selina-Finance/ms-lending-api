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

package com.selina.lending.internal.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.mapper.DIPApplicationRequestMapper;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

@Service
public class LendingServiceImpl implements LendingService {

    private final MiddlewareRepository middlewareRepository;
    private final TokenService tokenService;

    public LendingServiceImpl(MiddlewareRepository middlewareRepository, TokenService tokenService) {
        this.middlewareRepository = middlewareRepository;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<ApplicationDecisionResponse> getApplication(String id) {
        return middlewareRepository.getApplicationById(id);
    }

    @Override
    public void updateDipApplication(String id, DIPApplicationRequest dipApplicationRequest) {
        middlewareRepository.updateDipApplication(id, DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest));
    }

    @Override
    public ApplicationResponse createDipApplication(DIPApplicationRequest dipApplicationRequest) {
        var applicationRequest = DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest);
        applicationRequest.setSourceAccount(tokenService.retrieveSourceAccount());
        return middlewareRepository.createDipApplication(applicationRequest);
    }
}
