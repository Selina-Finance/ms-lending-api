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

import com.selina.lending.messaging.event.middleware.MiddlewareCreateApplicationEvent;
import com.selina.lending.messaging.publisher.MiddlewareCreateApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.selina.lending.internal.repository.SelectionServiceRepository;
import com.selina.lending.internal.service.application.domain.quote.FilterQuickQuoteApplicationRequest;
import com.selina.lending.internal.service.application.domain.quote.FilteredQuickQuoteDecisionResponse;

@Service
public class FilterApplicationServiceImpl implements FilterApplicationService {

    private final MiddlewareCreateApplicationEventPublisher eventPublisher;
    private final SelectionServiceRepository selectionServiceRepository;

    public FilterApplicationServiceImpl(MiddlewareCreateApplicationEventPublisher eventPublisher,
                                        SelectionServiceRepository selectionServiceRepository) {
        this.eventPublisher = eventPublisher;
        this.selectionServiceRepository = selectionServiceRepository;
    }

    @Override
    public FilteredQuickQuoteDecisionResponse filter(MiddlewareCreateApplicationEvent applicationEvent,
                                                     FilterQuickQuoteApplicationRequest request) {
        var decisionResponse = selectionServiceRepository.filter(request);
        eventPublisher.publish(applicationEvent);
        return decisionResponse;
    }
}
