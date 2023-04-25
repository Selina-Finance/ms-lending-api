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

import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.repository.SelectionServiceRepository;
import com.selina.lending.internal.service.application.domain.quote.FilterQuickQuoteApplicationRequest;
import com.selina.lending.internal.service.application.domain.quote.FilteredQuickQuoteDecisionResponse;
import com.selina.lending.messaging.event.middleware.MiddlewareCreateApplicationEvent;
import com.selina.lending.messaging.mapper.middleware.MiddlewareCreateApplicationEventMapper;
import com.selina.lending.messaging.publisher.MiddlewareCreateApplicationEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterApplicationServiceImplTest {

    @Mock
    private SelectionServiceRepository selectionServiceRepository;

    @Mock
    private MiddlewareCreateApplicationEventPublisher eventPublisher;

    @Mock
    private MiddlewareCreateApplicationEventMapper createApplicationEventMapper;

    @Mock
    private MiddlewareCreateApplicationEvent createApplicationEvent;

    @Mock
    private QuickQuoteApplicationRequest quickQuoteApplicationRequest;

    @Mock
    private FilteredQuickQuoteDecisionResponse filteredQuickQuoteDecisionResponse;

    @InjectMocks
    private FilterApplicationServiceImpl filterApplicationService;

    @Test
    void shouldFilterQuickQuoteApplicationAndSendMiddlewareCreateApplicationEvent() {
        //Given
        when(selectionServiceRepository.filter(any(FilterQuickQuoteApplicationRequest.class))).thenReturn(filteredQuickQuoteDecisionResponse);
        when(createApplicationEventMapper.mapToMiddlewareCreateApplicationEvent(quickQuoteApplicationRequest)).thenReturn(createApplicationEvent);
        doNothing().when(eventPublisher).publish(eq(createApplicationEvent));

        //When
        var response = filterApplicationService.filter(quickQuoteApplicationRequest);

        //Then
        InOrder inOrder = inOrder(selectionServiceRepository, eventPublisher);
        inOrder.verify(selectionServiceRepository, times(1)).filter(any(FilterQuickQuoteApplicationRequest.class));
        inOrder.verify(eventPublisher, times(1)).publish(createApplicationEvent);
        assertThat(response).isEqualTo(filteredQuickQuoteDecisionResponse);
    }
}