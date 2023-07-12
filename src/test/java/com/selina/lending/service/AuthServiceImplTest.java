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

package com.selina.lending.service;

import com.selina.lending.api.dto.auth.request.Credentials;
import com.selina.lending.api.dto.auth.response.TokenResponse;
import com.selina.lending.repository.auth.KeycloakRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private KeycloakRepository authRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldProxyToAuthRepositoryWhenGetTokenByCredentials() {
        //Given
        var credentials = new Credentials("the-client-id", "client-super-secret");

        var repoResponse = new TokenResponse("token", 100);
        when(authRepository.getTokenByCredentials(credentials)).thenReturn(repoResponse);

        //When
        var result = authService.getTokenByCredentials(credentials);

        //Then
        assertThat(result.accessToken()).isEqualTo(repoResponse.accessToken());
        assertThat(result.expiresIn()).isEqualTo(repoResponse.expiresIn());

        verify(authRepository, times(1)).getTokenByCredentials(credentials);
    }
}