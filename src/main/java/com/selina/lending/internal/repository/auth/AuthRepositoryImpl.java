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
import com.selina.lending.internal.dto.auth.CredentialsDto;
import com.selina.lending.internal.service.application.domain.auth.LoginResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthApi authApi;

    public AuthRepositoryImpl(AuthApi authApi) {
        this.authApi = authApi;
    }

    @Override
    public LoginResponse getTokenByCredentials(CredentialsDto credentialsDto) {

        return authApi.login(Map.of(
                        "client_id", credentialsDto.clientId(),
                        "client_secret", credentialsDto.clientSecret(),
                        "grant_type", "client_credentials"
                )
        );
    }
}
