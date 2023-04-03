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

import com.selina.lending.internal.mapper.QuickQuoteCCRequestMapper;
import com.selina.lending.internal.mapper.QuickQuoteCCResponseMapper;
import com.selina.lending.internal.service.CreateApplicationService;
import com.selina.lending.internal.service.application.domain.quotecc.QuickQuoteCCResponse;
import com.selina.lending.internal.service.permissions.annotation.Permission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.dto.quote.QuickQuoteResponse;
import com.selina.lending.internal.mapper.quote.QuickQuoteApplicationRequestMapper;
import com.selina.lending.internal.mapper.quote.QuickQuoteApplicationResponseMapper;
import com.selina.lending.internal.service.FilterApplicationService;

import lombok.extern.slf4j.Slf4j;

import static com.selina.lending.internal.service.permissions.annotation.Permission.Resource.QQ;
import static com.selina.lending.internal.service.permissions.annotation.Permission.Resource.QQ_CC;
import static com.selina.lending.internal.service.permissions.annotation.Permission.Scope.Create;
import static com.selina.lending.internal.service.permissions.annotation.Permission.Scope.Update;

@RestController
@Slf4j
public class QuickQuoteController implements QuickQuoteOperations {

    private final FilterApplicationService filterApplicationService;

    private final CreateApplicationService createApplicationService;

    public QuickQuoteController(FilterApplicationService filterApplicationService, CreateApplicationService createApplicationService) {
        this.filterApplicationService = filterApplicationService;
        this.createApplicationService = createApplicationService;
    }

    @Override
    @Permission(resource = QQ, scope = Create)
    public ResponseEntity<QuickQuoteResponse> createQuickQuoteApplication(
            QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        log.info("Create Quick Quote application with [externalApplicationId={}]", quickQuoteApplicationRequest.getExternalApplicationId());
        return ResponseEntity.ok(filterQuickQuote(quickQuoteApplicationRequest));
    }

    @Override
    @Permission(resource = QQ_CC, scope = Create)
    public ResponseEntity<QuickQuoteResponse> createQuickQuoteCCApplication(
            QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        log.info("Create Quick Quote CC application with [externalApplicationId={}]", quickQuoteApplicationRequest.getExternalApplicationId());
        var x = filterQuickQuoteCC(quickQuoteApplicationRequest);
        return ResponseEntity.ok(x);
    }

    @Override
    @Permission(resource = QQ, scope = Update)
    public ResponseEntity<QuickQuoteResponse> updateQuickQuoteApplication(String externalApplicationId,
            QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        log.info("Update Quick Quote application with [externalApplicationId={}]", quickQuoteApplicationRequest.getExternalApplicationId());
        if (!externalApplicationId.equals(quickQuoteApplicationRequest.getExternalApplicationId())) {
            throw new AccessDeniedException(AccessDeniedException.ACCESS_DENIED_MESSAGE + " " + externalApplicationId);
        }
        return ResponseEntity.ok(filterQuickQuote(quickQuoteApplicationRequest));
    }


    private QuickQuoteResponse filterQuickQuoteCC(QuickQuoteApplicationRequest quickQuoteApplicationRequest){
        QuickQuoteCCResponse quickQuoteDecisionResponse = createApplicationService.createQuickQuoteCCApplication(QuickQuoteCCRequestMapper.INSTANCE
                .mapToQuickQuoteCCRequest(quickQuoteApplicationRequest));
        QuickQuoteResponse quickQuoteResponse = QuickQuoteCCResponseMapper.INSTANCE.mapToQuickQuoteResponse(quickQuoteDecisionResponse);
        enrichResponseWithExternalApplicationId(quickQuoteResponse, quickQuoteApplicationRequest.getExternalApplicationId());
        return quickQuoteResponse;
    }

    private QuickQuoteResponse filterQuickQuote(QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        var filteredQuickQuoteDecisionResponse = filterApplicationService.filter(QuickQuoteApplicationRequestMapper.mapRequest(
                quickQuoteApplicationRequest));
        var quickQuoteResponse = QuickQuoteApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(filteredQuickQuoteDecisionResponse);
        enrichResponseWithExternalApplicationId(quickQuoteResponse, quickQuoteApplicationRequest.getExternalApplicationId());
        return quickQuoteResponse;
    }

    private void enrichResponseWithExternalApplicationId(QuickQuoteResponse response, String externalApplicationId) {
        response.setExternalApplicationId(externalApplicationId);
    }
}
