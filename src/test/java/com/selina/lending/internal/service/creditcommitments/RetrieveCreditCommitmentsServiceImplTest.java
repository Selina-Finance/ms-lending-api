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

package com.selina.lending.internal.service.creditcommitments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.internal.repository.CreditCommitmentsRepository;
import com.selina.lending.internal.repository.MiddlewareApplicationServiceRepository;
import com.selina.lending.internal.service.AccessManagementService;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.creditcommitments.CreditCommitmentResponse;

@ExtendWith(MockitoExtension.class)
class RetrieveCreditCommitmentsServiceImplTest {
    private static final String APPLICATION_ID = "appId";
    private static final String EXTERNAL_APPLICATION_ID = "externalCaseId";
    private static final String SOURCE_ACCOUNT = "sourceAccount";
    @Mock
    private CreditCommitmentsRepository commitmentsRepository;

    @Mock
    private ApplicationIdentifier applicationIdentifier;
    @Mock
    private CreditCommitmentResponse response;

    @Mock
    private AccessManagementService accessManagementService;

    @Mock
    private MiddlewareApplicationServiceRepository middlewareApplicationServiceRepository;

    @InjectMocks
    private RetrieveCreditCommitmentsServiceImpl service;

    @Test
    void getCreditCommitments() {
        //Given
        when(middlewareApplicationServiceRepository.getAppIdByExternalId(EXTERNAL_APPLICATION_ID)).thenReturn(applicationIdentifier);
        when(applicationIdentifier.getSourceAccount()).thenReturn(SOURCE_ACCOUNT);
        when(applicationIdentifier.getId()).thenReturn(APPLICATION_ID);
        doNothing().when(accessManagementService).checkSourceAccountAccessPermitted(SOURCE_ACCOUNT);
        when(commitmentsRepository.getCreditCommitments(APPLICATION_ID)).thenReturn(response);

        //When
        var result = service.getCreditCommitments(EXTERNAL_APPLICATION_ID);

        //Then
        assertEquals(result, response);
        verify(commitmentsRepository, times(1)).getCreditCommitments(APPLICATION_ID);
    }
}