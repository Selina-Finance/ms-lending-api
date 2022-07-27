/*
 *  Copyright 2022 Selina Finance
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.selina.lending.api.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selina.lending.internal.dto.ApplicationDecisionResponse;
import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.mapper.ApplicationDecisionResponseMapper;
import com.selina.lending.internal.mapper.ApplicationResponseMapper;
import com.selina.lending.internal.service.LendingService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/application")
@Slf4j
public class LendingController implements LendingOperations {

    private final LendingService lendingService;

    public LendingController(LendingService lendingService) {
        this.lendingService = lendingService;
    }

    @Override
    public ResponseEntity<ApplicationDecisionResponse> getApplication(String id) {
        log.info("LendingController get application {}", id);
        Optional<com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse> applicationResponse = lendingService.getApplication(id);
        if (applicationResponse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(toApplicationDecisionResponseDto(applicationResponse.get()));
    }

    @Override
    public ResponseEntity updateDipApplication(String id, DIPApplicationRequest dipApplicationRequest) {
        log.info("LendingController update dip application {}", id);
        com.selina.lending.internal.service.application.domain.ApplicationResponse applicationResponse = lendingService.updateDipApplication(id, dipApplicationRequest);
        return ResponseEntity.ok().body(toApplicationResponseDto(applicationResponse));
    }

    @Override
    public ResponseEntity<ApplicationResponse> createDipApplication(@Valid DIPApplicationRequest dipApplicationRequest) {
        com.selina.lending.internal.service.application.domain.ApplicationResponse applicationResponse = lendingService.createDipApplication(dipApplicationRequest);
        return ResponseEntity.ok().body(toApplicationResponseDto(applicationResponse));
    }

    private ApplicationResponse toApplicationResponseDto(
            com.selina.lending.internal.service.application.domain.ApplicationResponse applicationResponse) {
        return ApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse);
    }

    private ApplicationDecisionResponse toApplicationDecisionResponseDto(com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse applicationDecisionResponse) {
        return ApplicationDecisionResponseMapper.INSTANCE.mapToApplicationDecisionResponseDto(applicationDecisionResponse);
    }
}
