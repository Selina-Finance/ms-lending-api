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

package com.selina.lending.internal.repository;

import org.springframework.stereotype.Service;

import com.selina.lending.httpclient.selection.SelectionApi;
import com.selina.lending.internal.dto.LendingConstants;
import com.selina.lending.internal.service.TokenService;
import com.selina.lending.httpclient.selection.dto.request.FilterQuickQuoteApplicationRequest;
import com.selina.lending.httpclient.selection.dto.response.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.httpclient.selection.dto.request.Source;
import com.selina.lending.httpclient.selection.dto.request.SourceAccount;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SelectionRepositoryImpl implements SelectionRepository {
    private final SelectionApi selectionApi;

    private final TokenService tokenService;

    public SelectionRepositoryImpl(SelectionApi selectionApi, TokenService tokenService) {
        this.selectionApi = selectionApi;
        this.tokenService = tokenService;
    }

    @Override
    public FilteredQuickQuoteDecisionResponse filter(FilterQuickQuoteApplicationRequest request) {
        log.info("Filter Quick Quote application [externalApplicationId={}]",
                request.getApplication().getExternalApplicationId());
        enrichRequest(request);
        return selectionApi.filterQuickQuote(request);
    }

    private void enrichRequest(FilterQuickQuoteApplicationRequest request) {
        var application = request.getApplication();
        var source = Source.builder().name(LendingConstants.REQUEST_SOURCE).account(
                SourceAccount.builder().name(tokenService.retrieveSourceAccount()).build()).build();
        application.setSource(source);

        var partnerAccountId = tokenService.retrievePartnerAccountId();
        if (partnerAccountId != null) {
            application.setPartnerAccountId(partnerAccountId);
        }
    }
}
