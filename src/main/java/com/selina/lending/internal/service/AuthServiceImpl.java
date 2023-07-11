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

package com.selina.lending.internal.service;

import com.selina.lending.internal.dto.auth.TokenResponse;
import com.selina.lending.internal.dto.auth.Credentials;
import com.selina.lending.internal.repository.auth.KeycloakRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final KeycloakRepository repository;

    public AuthServiceImpl(KeycloakRepository repository) {
        this.repository = repository;
    }

    @Override
    public TokenResponse getTokenByCredentials(Credentials credentials) {
        return repository.getTokenByCredentials(credentials);
    }
}
