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

import com.selina.lending.httpclient.authorization.AuthorizationApi;
import com.selina.lending.httpclient.authorization.dto.request.GetPermissionsRequest;
import com.selina.lending.httpclient.authorization.dto.response.PermissionsResponse;
import com.selina.lending.httpclient.authorization.dto.response.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AuthorizationApiRepository implements AuthorizationRepository {

    private final AuthorizationApi authorizationApi;

    public AuthorizationApiRepository(AuthorizationApi authorizationApi) {
        this.authorizationApi = authorizationApi;
    }

    @Override
    public List<Resource> getByUserToken(String userToken) {
        try {
            var response = authorizationApi.getPermissions(new GetPermissionsRequest(userToken));
            return Optional.ofNullable(response)
                    .map(PermissionsResponse::resources)
                    .orElse(Collections.emptyList());
        } catch (Exception e) {
            log.error("Error while calling permissionsApi", e);
            return Collections.emptyList();
        }
    }
}
