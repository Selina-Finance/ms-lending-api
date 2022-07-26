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

import com.selina.lending.internal.api.AuthApi;
import com.selina.lending.internal.dto.auth.TokenResponse;
import com.selina.lending.internal.dto.auth.Credentials;
import com.selina.lending.internal.service.application.domain.auth.AuthApiTokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthRepositoryImplTest {

    @Mock
    private AuthApi authApi;

    @InjectMocks
    private AuthRepositoryImpl authRepository;

    @Test
    void shouldMapApiResponseWhenGetTokenByCredentialsInvoked() {
        // Given
        var credentials = new Credentials("the-client-id", "client-super-secret");
        var apiResponse = new AuthApiTokenResponse("auth-token", 300);
        when(authApi.login(any())).thenReturn(apiResponse);

        // When
        var result = authRepository.getTokenByCredentials(credentials);

        // Then
        assertThat(result).isEqualTo(new TokenResponse(apiResponse.access_token(), apiResponse.expires_in()));
    }

    @Test
    void shouldCallAuthApiWhenGetTokenByCredentialsInvoked() {
        // Given
        var credentials = new Credentials("the-client-id", "client-super-secret");
        when(authApi.login(any())).thenReturn(new AuthApiTokenResponse("auth-token", 300));

        var expectedAuthApiRequestParams = Map.of(
                "client_id", credentials.clientId(),
                "client_secret", credentials.clientSecret(),
                "grant_type", "client_credentials"
        );

        // When
        var result = authRepository.getTokenByCredentials(credentials);

        // Then
        verify(authApi, times(1)).login(expectedAuthApiRequestParams);
    }
}