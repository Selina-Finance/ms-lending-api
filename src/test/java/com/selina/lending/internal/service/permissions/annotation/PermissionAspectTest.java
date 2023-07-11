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
import com.selina.lending.internal.repository.auth.AuthorizationRepository;
import com.selina.lending.internal.service.permissions.PermissionServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissionAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private Permission permission;

    @Mock
    private AuthorizationRepository repository;
    @Mock
    private PermissionServiceImpl service;

    @InjectMocks
    private PermissionAspect aspect;

    @Test
    void shouldContinueProcessingWhenServiceAllowedAccess() throws Throwable {
        //Given
        when(permission.resource()).thenReturn(Permission.Resource.DIP);
        when(permission.scope()).thenReturn(Permission.Scope.Read);
        mockSecurity();
        when(service.isAccessDenied(any(), any())).thenReturn(false);

        //When
        aspect.permissionCheck(joinPoint, permission);

        //Then
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void shouldThrowAccessDeniedWhenServiceDeniedTheAccessToTheResource() throws Throwable {
        //Given
        when(permission.resource()).thenReturn(Permission.Resource.DIP);
        when(permission.scope()).thenReturn(Permission.Scope.Read);
        mockSecurity();
        when(service.isAccessDenied(any(), any())).thenReturn(true);

        //When
        var exception = assertThrows(AccessDeniedException.class, () -> aspect.permissionCheck(joinPoint, permission));

        //Then
        assertThat(
                exception.getMessage(),
                equalTo("Error processing request: Sorry, but you have no access to this resource")
        );
        verify(joinPoint, times(0)).proceed();
    }

    private void mockSecurity() {
        var authentication = Mockito.mock(Authentication.class);
        var jwt = Jwt.withTokenValue("abc").header("", "").claim("claimName", "claimValue").build();
        when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}