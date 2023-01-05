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

package com.selina.lending.config.security.permissions;

import com.selina.lending.internal.dto.permissions.ResourceDto;
import com.selina.lending.internal.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.selina.lending.config.security.SecurityConfig.LOGIN_URL;

@Slf4j
@Component
public class PermissionsVoter implements AccessDecisionVoter {

    private final PermissionService permissionService;

    public PermissionsVoter(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        try {
            String url = ((FilterInvocation) object).getHttpRequest().getServletPath();
            String method = ((FilterInvocation) object).getHttpRequest().getMethod();

            if (LOGIN_URL.equals(url)) {
                return ACCESS_ABSTAIN; // not granted or not deny. The decision will be based on other voters
            }

            return isGranted((JwtAuthenticationToken) authentication, new ResourceDto(method, url));
        } catch (Exception e) {
            log.error("Can't parse input data to make an authorization decision");
        }
        return ACCESS_ABSTAIN; // not granted or not deny. The decision will be based on other voters
    }

    private Integer isGranted(JwtAuthenticationToken authentication, ResourceDto resource) {
        String userToken = authentication.getToken().getTokenValue();
        return permissionService.isAccessDenied(resource, userToken) ? ACCESS_DENIED : ACCESS_GRANTED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }
}
