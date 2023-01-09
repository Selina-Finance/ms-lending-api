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

import com.selina.lending.internal.api.PermissionsApi;
import com.selina.lending.internal.dto.auth.GetPermissionsRequest;
import com.selina.lending.internal.service.application.domain.auth.authorization.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PermissionsApiRepository implements PermissionsRepository {

    private final PermissionsApi permissionsApi;

    public PermissionsApiRepository(PermissionsApi permissionsApi) {
        this.permissionsApi = permissionsApi;
    }

    @Override
    public List<Resource> getByUserToken(String userToken) {
        return new ArrayList<>();
//        return permissionsApi.getPermissions(new GetPermissionsRequest(userToken));
    }
}
