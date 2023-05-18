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

package com.selina.lending.internal.service;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicantDto;
import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.mapper.quote.QuickQuoteApplicationRequestMapper;
import com.selina.lending.internal.mapper.quote.middleware.MiddlewareQuickQuoteApplicationRequestMapper;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.repository.SelectionServiceRepository;
import com.selina.lending.internal.service.application.domain.quote.selection.FilteredQuickQuoteDecisionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterApplicationServiceImpl implements FilterApplicationService {

    private static final String ACCEPTED_DECISION = "Accepted";

    private final MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper;
    private final SelectionServiceRepository selectionServiceRepository;
    private final MiddlewareRepository middlewareRepository;

    public FilterApplicationServiceImpl(MiddlewareQuickQuoteApplicationRequestMapper middlewareQuickQuoteApplicationRequestMapper,
                                        SelectionServiceRepository selectionServiceRepository,
                                        MiddlewareRepository middlewareRepository) {
        this.middlewareQuickQuoteApplicationRequestMapper = middlewareQuickQuoteApplicationRequestMapper;
        this.selectionServiceRepository = selectionServiceRepository;
        this.middlewareRepository = middlewareRepository;
    }

    @Override
    public FilteredQuickQuoteDecisionResponse filter(QuickQuoteApplicationRequest request) {
        FilteredQuickQuoteDecisionResponse decisionResponse =
                selectionServiceRepository.filter(QuickQuoteApplicationRequestMapper.mapRequest(request));

        if (ACCEPTED_DECISION.equalsIgnoreCase(decisionResponse.getDecision())
                && decisionResponse.getProducts() != null) {
            setDefaultApplicantPrimaryApplicantIfDoesNotExist(request);
            middlewareRepository.createQuickQuoteApplication(
                    middlewareQuickQuoteApplicationRequestMapper.mapToQuickQuoteRequest(request, decisionResponse.getProducts()));
        }
        return decisionResponse;
    }

    private void setDefaultApplicantPrimaryApplicantIfDoesNotExist(QuickQuoteApplicationRequest request) {
        if(haveNotPrimaryApplicant(request.getApplicants())){
            request.getApplicants().stream().findFirst()
                    .ifPresent(quickQuoteApplicant -> quickQuoteApplicant.setPrimaryApplicant(true));
        }
    }

    private boolean haveNotPrimaryApplicant(List<QuickQuoteApplicantDto> quickQuoteApplicants){
        return quickQuoteApplicants
                .stream()
                .noneMatch(quickQuoteApplicant -> quickQuoteApplicant.getPrimaryApplicant() != null
                        && quickQuoteApplicant.getPrimaryApplicant());
    }
}
