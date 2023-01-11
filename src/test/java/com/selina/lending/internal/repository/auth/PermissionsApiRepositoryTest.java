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

package com.selina.lending.internal.repository.auth;

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;
import com.selina.lending.internal.api.PermissionsApi;
import com.selina.lending.internal.dto.auth.GetPermissionsRequest;
import com.selina.lending.internal.service.application.domain.auth.authorization.PermissionsResponse;
import com.selina.lending.internal.service.application.domain.auth.authorization.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissionsApiRepositoryTest {

    @Mock
    private PermissionsApi permissionsApi;

    @InjectMocks
    private PermissionsApiRepository repository;

    @Test
    void shouldCallPermissionsApiWhenGetTokenByCredentialsInvoked() {
        // Given
        String userToken = "token";
        when(permissionsApi.getPermissions(any())).thenReturn(new PermissionsResponse(new ArrayList<>()));

        var expectedPermissionsRequest = new GetPermissionsRequest(userToken);

        // When
        var result = repository.getByUserToken(userToken);

        // Then
        verify(permissionsApi, times(1)).getPermissions(expectedPermissionsRequest);
    }

    @Test
    void shouldReturnThePermissionsList() {
        // Given
        var userToken = "token";
        var permittedResources = List.of(new Resource("a", null), new Resource("b", null));
        var apiResponse = new PermissionsResponse(permittedResources);
        when(permissionsApi.getPermissions(any())).thenReturn(apiResponse);

        // When
        var result = repository.getByUserToken(userToken);

        // Then
        assertThat(result).isEqualTo(permittedResources);
    }

    @Test
    void shouldReturnEmptyListWhenExceptionOccurs() {
        // Given
        var userToken = "token";
        when(permissionsApi.getPermissions(any())).thenThrow(new RemoteResourceProblemException());

        // When
        var result = repository.getByUserToken(userToken);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenResponseIsNull() {
        // Given
        var userToken = "token";
        when(permissionsApi.getPermissions(any())).thenReturn(null);

        // When
        var result = repository.getByUserToken(userToken);

        // Then
        assertThat(result).isEmpty();
    }

}