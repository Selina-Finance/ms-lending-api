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

import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.dto.creditcommitments.UpdateCreditCommitmentsRequest;
import com.selina.lending.internal.mapper.ApplicationResponseMapper;
import com.selina.lending.internal.service.creditcommitments.UpdateCreditCommitmentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class CreditCommitmentsController implements CreditCommitmentsOperations {

    private final UpdateCreditCommitmentsService service;

    public CreditCommitmentsController(UpdateCreditCommitmentsService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ApplicationResponse> updateCreditCommitments(
            String externalApplicationId,
            UpdateCreditCommitmentsRequest request) {
        log.info("Patch CreditCommitments with [externalApplicationId={}]", externalApplicationId);

        var response = service.patchCreditCommitments(externalApplicationId, request);
        return ResponseEntity.ok(ApplicationResponseMapper.INSTANCE.mapToApplicationResponseDto(response));
    }

    @Override
    public ResponseEntity<Resource> downloadEsisDoc(String externalApplicationId) throws IOException {
        log.info("Request to fetch ESIS pdf with [externalApplicationId={}]", externalApplicationId);

        Resource resource = esisDocService.getByExternalAppId(externalApplicationId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
