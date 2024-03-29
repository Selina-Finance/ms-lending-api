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

import com.selina.lending.api.dto.creditcommitments.request.UpdateCreditCommitmentsRequest;
import com.selina.lending.api.dto.creditcommitments.response.CreditCommitmentResponse;
import com.selina.lending.api.mapper.creditcommitments.CreditCommitmentResponseMapper;
import com.selina.lending.api.mapper.creditcommitments.UpdateCreditCommitmentsRequestMapper;
import com.selina.lending.service.creditcommitments.EsisDocService;
import com.selina.lending.service.creditcommitments.RetrieveCreditCommitmentsService;
import com.selina.lending.service.creditcommitments.UpdateCreditCommitmentsService;
import com.selina.lending.service.permissions.annotation.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.selina.lending.service.permissions.annotation.Permission.Resource.CC;
import static com.selina.lending.service.permissions.annotation.Permission.Scope.Read;
import static com.selina.lending.service.permissions.annotation.Permission.Scope.Update;

@Slf4j
@RestController
public class CreditCommitmentsController implements CreditCommitmentsOperations {

    private final UpdateCreditCommitmentsService updateCreditCommitmentsService;
    private final RetrieveCreditCommitmentsService retrieveCreditCommitmentsService;
    private final EsisDocService esisDocService;

    public CreditCommitmentsController(UpdateCreditCommitmentsService updateCreditCommitmentsService, EsisDocService esisDocService,
            RetrieveCreditCommitmentsService retrieveCreditCommitmentsService) {
        this.updateCreditCommitmentsService = updateCreditCommitmentsService;
        this.esisDocService = esisDocService;
        this.retrieveCreditCommitmentsService = retrieveCreditCommitmentsService;
    }

    @Override
    @Permission(resource = CC, scope = Update)
    public ResponseEntity<Void> updateCreditCommitments(
            String externalApplicationId,
            UpdateCreditCommitmentsRequest request) {
        log.info("Update CreditCommitments with [externalApplicationId={}]", externalApplicationId);

        updateCreditCommitmentsService.updateCreditCommitments(externalApplicationId, UpdateCreditCommitmentsRequestMapper.INSTANCE.mapToUpdateCreditCommitmentsRequest(request));
        return ResponseEntity.noContent().build();
    }

    @Override
    @Permission(resource = CC, scope = Read)
    public ResponseEntity<Resource> downloadEsis(String externalApplicationId) throws IOException {
        log.info("Request to fetch ESIS pdf with [externalApplicationId={}]", externalApplicationId);

        Resource resource = esisDocService.getByExternalAppId(externalApplicationId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @Override
    @Permission(resource = CC, scope = Read)
    public ResponseEntity<CreditCommitmentResponse> getCreditCommitments(String externalApplicationId) {
        log.info("Get CreditCommitments with [externalApplicationId={}]", externalApplicationId);

        var response = retrieveCreditCommitmentsService.getCreditCommitments(externalApplicationId);
        return ResponseEntity.ok(CreditCommitmentResponseMapper.INSTANCE.mapToCreditCommitmentResponse(response));
    }
}
