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

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.repository.CreditCommitmentsRepository;
import com.selina.lending.internal.repository.GetApplicationRepository;
import com.selina.lending.internal.repository.MiddlewareRepository;
import com.selina.lending.internal.service.AccessManagementService;
import com.selina.lending.httpclient.getapplication.dto.response.ApplicationIdentifier;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.httpclient.creditcommitments.dto.response.PatchCreditCommitmentResponse;
import com.selina.lending.httpclient.creditcommitments.dto.request.UpdateCreditCommitmentsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.selina.lending.api.errors.custom.AccessDeniedException.ACCESS_DENIED_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@ExtendWith(MockitoExtension.class)
class UpdateCreditCommitmentsServiceImplTest {
    @Mock
    private MiddlewareRepository middlewareRepository;
    @Mock
    private GetApplicationRepository applicationRepository;
    @Mock
    private CreditCommitmentsRepository commitmentsRepository;
    @Mock
    private AccessManagementService accessManagementService;

    @Mock
    private ApplicationResponse applicationResponse;

    @InjectMocks
    private UpdateCreditCommitmentsServiceImpl service;

    @Test
    void shouldInvokeRemoteResourcesInOrderWhenPatchCreditCommitments() {
        // Given
        var externalId = UUID.randomUUID().toString();
        var request = UpdateCreditCommitmentsRequest.builder().build();

        var applicationIdentifier = new ApplicationIdentifier("the-app-id-abc", "sourceAccount");
        when(applicationRepository.getAppIdByExternalId(any())).thenReturn(applicationIdentifier);

        doNothing().when(accessManagementService).checkSourceAccountAccessPermitted(any());

        when(commitmentsRepository.patchCreditCommitments(any(), any())).thenReturn(new PatchCreditCommitmentResponse());
        when(middlewareRepository.checkAffordability(anyString())).thenReturn(applicationResponse);

        // When
        service.updateCreditCommitments(externalId, request);

        // Then
        verify(applicationRepository, times(1)).getAppIdByExternalId(externalId);
        verify(accessManagementService, times(1)).checkSourceAccountAccessPermitted(applicationIdentifier.getSourceAccount());
        verify(commitmentsRepository, times(1)).patchCreditCommitments(applicationIdentifier.getId(), request);
        verify(middlewareRepository, times(1)).checkAffordability(applicationIdentifier.getId());
    }

    @Test
    void shouldThrowExceptionWhenClientHasNoPermitToManageTheCreditCommitments() {
        // Given
        var externalId = UUID.randomUUID().toString();
        var request = UpdateCreditCommitmentsRequest.builder().build();

        var identifier = new ApplicationIdentifier("the-app-id-abc", "the-source-account-id");
        when(applicationRepository.getAppIdByExternalId(any())).thenReturn(identifier);

        doThrow(new AccessDeniedException(ACCESS_DENIED_MESSAGE))
                .when(accessManagementService).checkSourceAccountAccessPermitted(any());

        // When
        var exception = assertThrows(
                AccessDeniedException.class,
                () -> service.updateCreditCommitments(externalId, request)
        );

        //Then
        assertThat(exception.getStatus().getReasonPhrase()).isEqualTo(FORBIDDEN.getReasonPhrase());
        verify(applicationRepository, times(1)).getAppIdByExternalId(externalId);
        verify(accessManagementService, times(1)).checkSourceAccountAccessPermitted(identifier.getSourceAccount());
        verify(commitmentsRepository, times(0)).patchCreditCommitments(identifier.getId(), request);
    }
}