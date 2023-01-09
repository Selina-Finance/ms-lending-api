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

import com.selina.lending.internal.dto.permissions.AskedResourceDto;
import com.selina.lending.internal.repository.auth.PermissionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.selina.lending.internal.service.permissions.helpers.HttpMethodToAuthTranslationHelper.toAuthScope;
import static com.selina.lending.internal.service.permissions.helpers.HttpUrlToAuthTranslationHelper.toAuthResourceName;
import static com.selina.lending.internal.service.permissions.helpers.PermissionsCalculationHelper.isUserResourcesContainsAskedResource;


@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionsRepository repository;

    public PermissionServiceImpl(PermissionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isAccessDenied(AskedResourceDto resource, String userToken) {
        log.info("Checking access to resource: {}", resource);
        var userResources = repository.getByUserToken(userToken);
        return !isUserResourcesContainsAskedResource(
                userResources,
                toAuthResourceName(resource.url()),
                toAuthScope(resource.method())
        );
    }

}
