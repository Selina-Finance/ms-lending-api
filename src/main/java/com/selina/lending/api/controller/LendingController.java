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

package com.selina.lending.api.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.selina.lending.internal.dto.ApplicationDecisionResponse;
import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.mapper.ApplicationDecisionResponseMapper;
import com.selina.lending.internal.mapper.ApplicationResponseMapper;
import com.selina.lending.internal.mapper.DIPApplicationRequestMapper;
import com.selina.lending.internal.service.CreateApplicationService;
import com.selina.lending.internal.service.RetrieveApplicationService;
import com.selina.lending.internal.service.UpdateApplicationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LendingController implements LendingOperations {

    private final RetrieveApplicationService retrieveApplicationService;
    private final CreateApplicationService createApplicationService;
    private final UpdateApplicationService updateApplicationService;

    public LendingController(RetrieveApplicationService retrieveApplicationService, CreateApplicationService createApplicationService, UpdateApplicationService updateApplicationService) {
        this.retrieveApplicationService = retrieveApplicationService;
        this.createApplicationService = createApplicationService;
        this.updateApplicationService = updateApplicationService;
    }

    @Override
    public ResponseEntity<ApplicationDecisionResponse> getApplication(String externalApplicationId) {
        log.info("Get application [externalApplicationId={}]", externalApplicationId);
        var applicationResponse = retrieveApplicationService.getApplicationByExternalApplicationId(externalApplicationId);
        return ResponseEntity.of(applicationResponse.map(ApplicationDecisionResponseMapper.INSTANCE::mapToApplicationDecisionResponseDto));
    }

    @Override
    public ResponseEntity<ApplicationResponse> updateDipCCApplication(String externalApplicationId, DIPApplicationRequest dipApplicationRequest) {
        log.info("Update DIPCC application [externalApplicationId={}]", externalApplicationId);
        var applicationResponse = updateApplicationService.updateDipCCApplication(externalApplicationId,
                DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest));
        return ResponseEntity.ok(ApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse));
    }

    @Override
    public ResponseEntity<ApplicationResponse> createDipCCApplication(@Valid DIPApplicationRequest dipApplicationRequest) {
        log.info("Create DIPCC application with [externalApplicationId={}]", dipApplicationRequest.getExternalApplicationId());
        var applicationResponse = createApplicationService.createDipCCApplication(DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest));
        return ResponseEntity.ok(ApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse));
    }

    @Override
    public ResponseEntity<ApplicationResponse> updateDipApplication(String externalApplicationId,
            DIPApplicationRequest dipApplicationRequest) {
        log.info("Update DIP application [externalApplicationId={}]", externalApplicationId);
        var applicationResponse = updateApplicationService.updateDipApplication(externalApplicationId,
                DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest));
        return ResponseEntity.ok(ApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse));
    }

    @Override
    public ResponseEntity<ApplicationResponse> createDipApplication(@Valid DIPApplicationRequest dipApplicationRequest) {
        log.info("Create DIP application with [externalApplicationId={}]", dipApplicationRequest.getExternalApplicationId());
        var applicationResponse = createApplicationService.createDipApplication(DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest));
        return ResponseEntity.ok(ApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse));
    }
}
