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

package com.selina.lending.internal.service.permissions;

import com.selina.lending.internal.dto.AskedResource;
import com.selina.lending.internal.service.application.domain.auth.authorization.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Override
    public boolean isAccessDenied(AskedResource asked, List<Resource> permitted) {
        log.debug("Checking access of asked resource: {} in permitted: {}", asked, permitted);

        return permitted.stream()
                .filter(resource -> resource.name().equals(asked.name()))
                .findFirst()
                .map(Resource::scopes)
                .filter(scopes -> !scopes.isEmpty())
                .filter(scopes -> scopes.contains(asked.scope()))
                .isEmpty();

    }
}
