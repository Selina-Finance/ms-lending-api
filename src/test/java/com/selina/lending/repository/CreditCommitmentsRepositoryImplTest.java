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

package com.selina.lending.repository;

import com.selina.lending.httpclient.creditcommitments.CreditCommitmentsApi;
import com.selina.lending.httpclient.creditcommitments.dto.request.UpdateCreditCommitmentsRequest;
import com.selina.lending.httpclient.creditcommitments.dto.response.CreditCommitmentResponse;
import com.selina.lending.httpclient.creditcommitments.dto.response.PatchCreditCommitmentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditCommitmentsRepositoryImplTest {

    @Mock
    private CreditCommitmentsApi creditCommitmentsApi;

    @Mock
    private CreditCommitmentResponse creditCommitmentResponse;

    @InjectMocks
    private CreditCommitmentsRepositoryImpl repository;

    @Test
    void shouldInvokeHttpClientWhenPatchCreditCommitments() {
        //Given
        var id = UUID.randomUUID().toString();
        var request = UpdateCreditCommitmentsRequest.builder().build();
        when(creditCommitmentsApi.patchCreditCommitments(any(), any())).thenReturn(null);

        //When
        repository.patchCreditCommitments(id, request);

        //Then
        verify(creditCommitmentsApi, times(1)).patchCreditCommitments(id, request);
    }

    @Test
    void shouldReturnPatchCreditCommitmentResponseWhenPatchCreditCommitments() {
        //Given
        var id = UUID.randomUUID().toString();
        var request = UpdateCreditCommitmentsRequest.builder().build();
        var response = PatchCreditCommitmentResponse.builder().id(id).build();
        when(creditCommitmentsApi.patchCreditCommitments(any(), any())).thenReturn(response);

        //When
        var result = repository.patchCreditCommitments(id, request);

        //Then
        assertEquals(result, response);
    }

    @Test
    void shouldReturnCreditCommitmentsResponseWhenGetCreditCommitments() {
        //Given
        var id = UUID.randomUUID().toString();
        when(creditCommitmentsApi.getCreditCommitments(id)).thenReturn(creditCommitmentResponse);

        //When
        var result = repository.getCreditCommitments(id);

        //Then
        assertEquals(result, creditCommitmentResponse);
        verify(creditCommitmentsApi, times(1)).getCreditCommitments(id);
    }
}