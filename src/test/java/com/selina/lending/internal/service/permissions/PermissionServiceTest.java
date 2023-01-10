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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @InjectMocks
    private PermissionServiceImpl permissionService;

    @Test
    void shouldDenyAccessWhenPermittedResourcesIsEmpty() {
        //Given
        var asked = new AskedResource(null, null);
        List<Resource> permitted = Collections.emptyList();

        //When
        var isDenied = permissionService.isAccessDenied(asked, permitted);

        //Then
        assertThat(isDenied).isTrue();
    }

    @Test
    void shouldDenyAccessWhenUserResourcesHaveNoMatchesByName() {
        //Given
        var asked = AskedResource.builder().name("DIP").build();
        List<Resource> permitted = List.of(
                Resource.builder().name("QQ").build()
        );

        //When
        var isDenied = permissionService.isAccessDenied(asked, permitted);

        //Then
        assertThat(isDenied).isTrue();
    }

    @Test
    void shouldDenyAccessWhenUserResourcesHaveMatchesByNameButDifferentScope() {
        //Given
        var asked = AskedResource.builder().name("DIP").scope("Write").build();
        List<Resource> permitted = List.of(
                Resource.builder().name("DIP").scopes(Set.of("Read")).build()
        );

        //When
        var isDenied = permissionService.isAccessDenied(asked, permitted);

        //Then
        assertThat(isDenied).isTrue();
    }

    @Test
    void shouldAllowAccessWhenAskedMatchesAndNameAndScopeOfPermitted() {
        //Given
        var asked = AskedResource.builder().name("DIP").scope("Write").build();
        List<Resource> permitted = List.of(
                Resource.builder().name("QQ").scopes(Set.of("Read", "Write", "Update")).build(),
                Resource.builder().name("DIP").scopes(Set.of("Read", "Write", "Update")).build()
        );

        //When
        var isDenied = permissionService.isAccessDenied(asked, permitted);

        //Then
        assertThat(isDenied).isFalse();
    }
}