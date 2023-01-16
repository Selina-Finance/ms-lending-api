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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import com.selina.lending.internal.dto.creditcommitments.request.UpdateCreditCommitmentsRequest;
import com.selina.lending.internal.mapper.MapperBase;
import com.selina.lending.internal.service.creditcommitments.EsisDocService;
import com.selina.lending.internal.service.creditcommitments.RetrieveCreditCommitmentsService;
import com.selina.lending.internal.service.creditcommitments.UpdateCreditCommitmentsService;

@ExtendWith(MockitoExtension.class)
class CreditCommitmentsControllerUnitTest extends MapperBase {

    @Mock
    private UpdateCreditCommitmentsService updateCreditCommitmentsService;

    @Mock
    private RetrieveCreditCommitmentsService retrieveCreditCommitmentsService;

    @Mock
    private EsisDocService esisDocService;

    @Mock
    private UpdateCreditCommitmentsRequest updateCreditCommitmentsRequest;

    @InjectMocks
    private CreditCommitmentsController controller;

    @Test
    void whenUpdateCreditCommitmentsThenCallUpdateCCService() {
        //Given
        var externalId = UUID.randomUUID().toString();
        doNothing().when(updateCreditCommitmentsService).updateCreditCommitments(any(), any());

        //When
        controller.updateCreditCommitments(externalId, updateCreditCommitmentsRequest);

        //Then
        verify(updateCreditCommitmentsService, times(1)).updateCreditCommitments(eq(externalId), any());
    }

    @Test
    void whenGetEsisCCDocThenCallGetEsisCCService() throws IOException {
        //Given
        var externalId = UUID.randomUUID().toString();

        var resource = new ByteArrayResource(new byte[0]);
        when(esisDocService.getByExternalAppId(any())).thenReturn(resource);

        //When
        var result = controller.downloadEsis(externalId);

        //Then
        assertNotNull(result.getBody());
        verify(esisDocService, times(1)).getByExternalAppId(externalId);
    }

    @Test
    void whenGetCreditCommitmentsThenCallGetCreditCommitmentsService() {
        //Given
        var externalId = UUID.randomUUID().toString();
        var response = getCreditCommitmentResponse();
        when(retrieveCreditCommitmentsService.getCreditCommitments(externalId)).thenReturn(response);

        //When
        var result = controller.getCreditCommitments(externalId);

        //Then
        assertNotNull(result.getBody());
        verify(retrieveCreditCommitmentsService, times(1)).getCreditCommitments(externalId);
    }
}