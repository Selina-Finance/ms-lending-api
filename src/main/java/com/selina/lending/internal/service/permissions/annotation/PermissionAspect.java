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

package com.selina.lending.internal.service.permissions.annotation;

import com.selina.lending.api.errors.custom.AccessDeniedException;
import com.selina.lending.internal.repository.auth.PermissionsRepository;
import com.selina.lending.internal.service.application.domain.auth.authorization.Resource;
import com.selina.lending.internal.service.permissions.PermissionService;
import com.selina.lending.internal.service.permissions.annotation.Permission;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Aspect
@Component
public class PermissionAspect {
    private final PermissionService service;
    private final PermissionsRepository repository;

    public PermissionAspect(PermissionService service,PermissionsRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Around("@annotation(permission)")
    public Object permissionCheck(ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userToken = (Jwt) authentication.getPrincipal();

        var userResources = repository.getByUserToken(userToken.getTokenValue());
        var askedResource = Resource.builder()
                .name(permission.resource())
                .scopes(Set.of(permission.scope()))
                .build();

        if (service.isAccessDenied(userResources, askedResource)) {
            throw new AccessDeniedException("Sorry, but you have no access to this resource");
        }

        return joinPoint.proceed();
    }
}
