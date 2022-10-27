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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import com.selina.lending.internal.mapper.quote.QuickQuoteApplicationRequestMapper;
import com.selina.lending.internal.mapper.quote.QuickQuoteApplicationResponseMapper;
import com.selina.lending.internal.service.FilterApplicationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class QuickQuoteController implements QuickQuoteOperations {

    private final FilterApplicationService filterApplicationService;

    public QuickQuoteController(FilterApplicationService filterApplicationService) {
        this.filterApplicationService = filterApplicationService;
    }

    @Override
    public ResponseEntity<QuickQuoteResponse> createQuickQuoteApplication(
            QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        log.info("Create Quick Quote application with [externalApplicationId={}]", quickQuoteApplicationRequest.getExternalApplicationId());
        var response = filterApplicationService.filter(QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest));
        return ResponseEntity.ok(QuickQuoteApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(response));
    }

    @Override
    public ResponseEntity<QuickQuoteResponse> updateQuickQuoteApplication(String externalApplicationId,
            QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        log.info("Update Quick Quote application with [externalApplicationId={}]", quickQuoteApplicationRequest.getExternalApplicationId());
        var response = filterApplicationService.filter(QuickQuoteApplicationRequestMapper.mapRequest(quickQuoteApplicationRequest));
        return ResponseEntity.ok(QuickQuoteApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(response));
    }
}
