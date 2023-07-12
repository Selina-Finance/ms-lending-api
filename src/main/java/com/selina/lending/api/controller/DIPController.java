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

import static com.selina.lending.service.permissions.annotation.Permission.Resource.APPLICATION;
import static com.selina.lending.service.permissions.annotation.Permission.Resource.DIP;
import static com.selina.lending.service.permissions.annotation.Permission.Resource.DIP_CC;
import static com.selina.lending.service.permissions.annotation.Permission.Scope.Create;
import static com.selina.lending.service.permissions.annotation.Permission.Scope.Read;
import static com.selina.lending.service.permissions.annotation.Permission.Scope.Update;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.selina.lending.api.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.api.dto.dipcc.response.DIPCCApplicationResponse;
import com.selina.lending.api.dto.dip.request.DIPApplicationRequest;
import com.selina.lending.api.dto.dip.response.DIPApplicationResponse;
import com.selina.lending.api.dto.dipcc.request.DIPCCApplicationRequest;
import com.selina.lending.internal.mapper.ApplicationDecisionResponseMapper;
import com.selina.lending.internal.mapper.DIPCCApplicationResponseMapper;
import com.selina.lending.internal.mapper.DIPApplicationRequestMapper;
import com.selina.lending.internal.mapper.DIPApplicationResponseMapper;
import com.selina.lending.internal.mapper.DIPCCApplicationRequestMapper;
import com.selina.lending.service.CreateApplicationService;
import com.selina.lending.service.RetrieveApplicationService;
import com.selina.lending.service.UpdateApplicationService;
import com.selina.lending.service.permissions.annotation.Permission;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DIPController implements DIPOperations {

    private final RetrieveApplicationService retrieveApplicationService;
    private final CreateApplicationService createApplicationService;
    private final UpdateApplicationService updateApplicationService;

    public DIPController(RetrieveApplicationService retrieveApplicationService, CreateApplicationService createApplicationService, UpdateApplicationService updateApplicationService) {
        this.retrieveApplicationService = retrieveApplicationService;
        this.createApplicationService = createApplicationService;
        this.updateApplicationService = updateApplicationService;
    }

    @Override
    @Permission(resource = APPLICATION, scope = Read)
    public ResponseEntity<ApplicationDecisionResponse> getApplication(String externalApplicationId) {
        log.info("Get application [externalApplicationId={}]", externalApplicationId);
        var applicationResponse = retrieveApplicationService.getApplicationByExternalApplicationId(externalApplicationId);
        return ResponseEntity.of(applicationResponse.map(ApplicationDecisionResponseMapper.INSTANCE::mapToApplicationDecisionResponseDto));
    }

    @Override
    @Permission(resource = DIP_CC, scope = Update)
    public ResponseEntity<Void> updateDipCCApplication(String externalApplicationId, DIPCCApplicationRequest request) {
        log.info("Update DIPCC application [externalApplicationId={}]", externalApplicationId);
        updateApplicationService.updateDipCCApplication(externalApplicationId,
                DIPCCApplicationRequestMapper.INSTANCE.mapToApplicationRequest(request));
        return ResponseEntity.noContent().build();
    }

    @Override
    @Permission(resource = DIP_CC, scope = Create)
    public ResponseEntity<DIPCCApplicationResponse> createDipCCApplication(@Valid DIPCCApplicationRequest request) {
        log.info("Create DIPCC application with [externalApplicationId={}]", request.getExternalApplicationId());
        var applicationResponse = createApplicationService.createDipCCApplication(DIPCCApplicationRequestMapper.INSTANCE.mapToApplicationRequest(request));
        return ResponseEntity.ok(DIPCCApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse));
    }

    @Override
    @Permission(resource = DIP, scope = Update)
    public ResponseEntity<Void> updateDipApplication(String externalApplicationId,
            DIPApplicationRequest dipApplicationRequest) {
        log.info("Update DIP application [externalApplicationId={}]", externalApplicationId);
        updateApplicationService.updateDipApplication(externalApplicationId,
                DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest));
        return ResponseEntity.noContent().build();
    }

    @Override
    @Permission(resource = DIP, scope = Create)
    public ResponseEntity<DIPApplicationResponse> createDipApplication(@Valid DIPApplicationRequest dipApplicationRequest) {
        log.info("Create DIP application with [externalApplicationId={}]", dipApplicationRequest.getExternalApplicationId());
        var applicationResponse = createApplicationService.createDipApplication(DIPApplicationRequestMapper.INSTANCE.mapToApplicationRequest(dipApplicationRequest));
        return ResponseEntity.ok(DIPApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse));
    }
}
