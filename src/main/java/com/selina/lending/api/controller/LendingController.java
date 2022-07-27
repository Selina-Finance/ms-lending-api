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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.service.LendingService;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

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
    public ResponseEntity getApplication(String id) {
        log.info("LendingController getApplication()");
        //TODO
        Optional<ApplicationDecisionResponse> applicationResponse = lendingService.getApplication(id);
        if (applicationResponse == null || applicationResponse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body("Get application for id "+id);
    }

    @Override
    public ResponseEntity updateDipApplication(String id, DIPApplicationRequest dipApplicationRequest) {
        log.info("LendingController updateDipApplication()");
        //TODO
        ApplicationResponse applicationResponse = lendingService.updateDipApplication(id, dipApplicationRequest);
        return ResponseEntity.ok().body("Update dip application for id "+ id);
    }

    @Override
    public ResponseEntity createDipApplication(@Valid DIPApplicationRequest dipApplicationRequest) {
        log.info("LendingController createDipApplication()");
        //TODO
        ApplicationResponse applicationResponse = lendingService.createDipApplication(dipApplicationRequest);
        return ResponseEntity.ok().body("Create new dip application");
    }
}
