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

import com.selina.lending.api.errors.custom.BadRequestException;
import com.selina.lending.internal.api.AuthApi;
import com.selina.lending.internal.dto.auth.Credentials;
import com.selina.lending.internal.dto.auth.TokenResponse;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthApi authApi;

    public AuthRepositoryImpl(AuthApi authApi) {
        this.authApi = authApi;
    }

    @Override
    public TokenResponse getTokenByCredentials(Credentials credentials) {
        try {
            var apiResponse = authApi.login(Map.of(
                            "client_id", credentials.clientId(),
                            "client_secret", credentials.clientSecret(),
                            "grant_type", "client_credentials"
                    )
            );
            return new TokenResponse(apiResponse.access_token(), apiResponse.expires_in());
        } catch (FeignException feignException) {
            if (feignException.status() == 400) {
                throw new BadRequestException(feignException.contentUTF8());
            }
            throw feignException;
        }

    }
}
