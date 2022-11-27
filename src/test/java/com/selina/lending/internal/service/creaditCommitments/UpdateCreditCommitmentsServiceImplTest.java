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

package com.selina.lending.internal.service.creaditCommitments;

import com.selina.lending.internal.dto.creaditCommitments.UpdateCreditCommitmentsRequest;
import com.selina.lending.internal.repository.CreditCommitmentsRepository;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.creditCommitments.PatchCCResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCreditCommitmentsServiceImplTest {
    @Mock
    private MiddlewareApplicationServiceRepository applicationRepository;
    @Mock
    private CreditCommitmentsRepository commitmentsRepository;

    @InjectMocks
    private UpdateCreditCommitmentsServiceImpl service;

    @Test
    void shouldInvokeRemoteResourcesInOrderWhenPatchCreditCommitments() {
        // Given
        var externalId = UUID.randomUUID().toString();
        var request = new UpdateCreditCommitmentsRequest();

        var identifier = new ApplicationIdentifier("the-app-id-abc", "the-source-account-id");
        when(applicationRepository.getAppIdByExternalId(any())).thenReturn(identifier);
        when(commitmentsRepository.patchCreditCommitments(any(),any())).thenReturn(new PatchCCResponse());

        // When
        service.patchCreditCommitments(externalId, request);

        // Then
        verify(applicationRepository, times(1)).getAppIdByExternalId(externalId);
        verify(commitmentsRepository, times(1)).patchCreditCommitments(identifier.getId(), request);
    }
}