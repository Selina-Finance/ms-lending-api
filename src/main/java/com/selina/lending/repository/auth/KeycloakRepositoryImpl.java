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

package com.selina.lending.repository.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selina.lending.api.dto.auth.request.Credentials;
import com.selina.lending.api.dto.auth.response.TokenResponse;
import com.selina.lending.exception.BadRequestException;
import com.selina.lending.exception.UnauthorizedException;
import com.selina.lending.httpclient.keycloak.KeycloakApi;
import feign.FeignException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KeycloakRepositoryImpl implements KeycloakRepository {

    private final KeycloakApi keycloakApi;
    private final ObjectMapper objectMapper;

    public KeycloakRepositoryImpl(KeycloakApi keycloakApi, ObjectMapper objectMapper) {
        this.keycloakApi = keycloakApi;
        this.objectMapper = objectMapper;
    }

    @Override
    public TokenResponse getTokenByCredentials(Credentials credentials) {
        try {
            var apiResponse = keycloakApi.login(Map.of(
                            "client_id", credentials.clientId(),
                            "client_secret", credentials.clientSecret(),
                            "grant_type", "client_credentials"
                    )
            );
            return new TokenResponse(apiResponse.access_token(), apiResponse.expires_in());
        } catch (FeignException feignException) {
            throw prepareException(feignException);
        }
    }

    private RuntimeException prepareException(FeignException feignException) {
        var details = getDetails(feignException);
        throw switch (feignException.status()) {
            case 400 -> new BadRequestException(details.description());
            case 401 -> new UnauthorizedException(details.description());
            default -> feignException;
        };
    }

    @SneakyThrows
    private KeycloakApi.ErrorDetails getDetails(FeignException feignException) {
        return objectMapper.readValue(feignException.contentUTF8(), KeycloakApi.ErrorDetails.class);
    }
}
