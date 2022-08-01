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

import com.selina.lending.internal.dto.ApplicationDecisionResponse;
import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.mapper.ApplicationDecisionResponseMapper;
import com.selina.lending.internal.mapper.ApplicationResponseMapper;
import com.selina.lending.internal.service.LendingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/application")
@Slf4j
public class LendingController implements LendingOperations {

    private final LendingService lendingService;

    public LendingController(LendingService lendingService) {
        this.lendingService = lendingService;
    }

    @Override
    public String securityTest(Jwt principal, String id) {
        log.info("principal: {}", principal.toString());
        log.info("principal name: {}", principal.getClaimAsString("preferred_username"));

//        throw new AccessDeniedException("test access denied!");
//        throw new InvalidBearerTokenException("token is invalid!");
        return "Success";
    }

    @Override
    public ResponseEntity<ApplicationDecisionResponse> getApplication(String id) {
        log.info("Get application {}", id);


        var applicationResponse = lendingService.getApplication(id);
        return ResponseEntity.of(applicationResponse.map(ApplicationDecisionResponseMapper.INSTANCE::mapToApplicationDecisionResponseDto));
    }

    @Override
    public ResponseEntity<Void> updateDipApplication(String id, DIPApplicationRequest dipApplicationRequest) {
        log.info("Update DIP application {}", id);
        lendingService.updateDipApplication(id, dipApplicationRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApplicationResponse> createDipApplication(@Valid DIPApplicationRequest dipApplicationRequest) {
        log.info("Create DIP application with externalApplicationId {}", dipApplicationRequest.getExternalApplicationId());
        var applicationResponse = lendingService.createDipApplication(dipApplicationRequest);
        return ResponseEntity.ok(ApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(applicationResponse));
    }
}
