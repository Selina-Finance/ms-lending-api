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

import com.selina.lending.exception.AccessDeniedException;
import com.selina.lending.api.dto.qq.request.QuickQuoteApplicationRequest;
import com.selina.lending.api.dto.qq.response.QuickQuoteResponse;
import com.selina.lending.internal.dto.quotecf.QuickQuoteCFApplicationRequest;
import com.selina.lending.internal.enricher.ApplicationResponseEnricher;
import com.selina.lending.internal.mapper.quote.QuickQuoteApplicationResponseMapper;
import com.selina.lending.internal.mapper.quotecf.QuickQuoteCFRequestMapper;
import com.selina.lending.internal.mapper.quotecf.QuickQuoteCFResponseMapper;
import com.selina.lending.internal.service.CreateApplicationService;
import com.selina.lending.internal.service.FilterApplicationService;
import com.selina.lending.httpclient.middleware.dto.qqcf.response.QuickQuoteCFResponse;
import com.selina.lending.internal.service.permissions.annotation.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.selina.lending.internal.service.permissions.annotation.Permission.Resource.QQ;
import static com.selina.lending.internal.service.permissions.annotation.Permission.Resource.QQ_CF;
import static com.selina.lending.internal.service.permissions.annotation.Permission.Scope.Create;
import static com.selina.lending.internal.service.permissions.annotation.Permission.Scope.Update;

@RestController
@Slf4j
public class QuickQuoteController implements QuickQuoteOperations {

    private final FilterApplicationService filterApplicationService;
    private final CreateApplicationService createApplicationService;
    private final ApplicationResponseEnricher applicationResponseEnricher;

    public QuickQuoteController(
            FilterApplicationService filterApplicationService,
            CreateApplicationService createApplicationService,
            ApplicationResponseEnricher applicationResponseEnricher
    ) {
        this.filterApplicationService = filterApplicationService;
        this.createApplicationService = createApplicationService;
        this.applicationResponseEnricher = applicationResponseEnricher;
    }

    @Override
    @Permission(resource = QQ, scope = Create)
    public ResponseEntity<QuickQuoteResponse> createQuickQuoteApplication(
            QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        log.info("Create Quick Quote application with [externalApplicationId={}]", quickQuoteApplicationRequest.getExternalApplicationId());
        return ResponseEntity.ok(filterQuickQuote(quickQuoteApplicationRequest));
    }

    @Override
    @Permission(resource = QQ_CF, scope = Create)
    public ResponseEntity<QuickQuoteResponse> createQuickQuoteCFApplication(
            QuickQuoteCFApplicationRequest quickQuoteCFApplicationRequest) {
        log.info("Create Quick Quote CF application with [externalApplicationId={}]", quickQuoteCFApplicationRequest.getExternalApplicationId());
        return ResponseEntity.ok(filterQuickQuoteCF(quickQuoteCFApplicationRequest));
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


    private QuickQuoteResponse filterQuickQuoteCF(QuickQuoteCFApplicationRequest quickQuoteCFApplicationRequest){
        QuickQuoteCFResponse quickQuoteDecisionResponse = createApplicationService.createQuickQuoteCFApplication(QuickQuoteCFRequestMapper.INSTANCE
                .mapToQuickQuoteCFRequest(quickQuoteCFApplicationRequest));
        QuickQuoteResponse quickQuoteResponse = QuickQuoteCFResponseMapper.INSTANCE.mapToQuickQuoteResponse(quickQuoteDecisionResponse);
        applicationResponseEnricher.enrichQuickQuoteResponseWithExternalApplicationId(quickQuoteResponse, quickQuoteCFApplicationRequest.getExternalApplicationId());
        return quickQuoteResponse;
    }

    private QuickQuoteResponse filterQuickQuote(QuickQuoteApplicationRequest quickQuoteApplicationRequest) {
        var filteredQuickQuoteDecisionResponse = filterApplicationService.filter(quickQuoteApplicationRequest);
        var quickQuoteResponse = QuickQuoteApplicationResponseMapper.INSTANCE.mapToQuickQuoteResponse(filteredQuickQuoteDecisionResponse);
        applicationResponseEnricher.enrichQuickQuoteResponseWithExternalApplicationId(quickQuoteResponse, quickQuoteApplicationRequest.getExternalApplicationId());
        applicationResponseEnricher.enrichQuickQuoteResponseWithProductOffersApplyUrl(quickQuoteResponse);
        return quickQuoteResponse;
    }
}
