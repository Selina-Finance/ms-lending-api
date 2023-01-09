///*
// * Copyright 2022 Selina Finance
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//
//package com.selina.lending.internal.service.permissions;
//
//import com.selina.lending.internal.dto.permissions.AskedResourceDto;
//import com.selina.lending.internal.repository.auth.PermissionsRepository;
//import com.selina.lending.internal.service.application.domain.auth.authorization.Resource;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.web.bind.annotation.RequestMethod.POST;
//
//@ExtendWith(MockitoExtension.class)
//class PermissionServiceTest {
//
//    @Mock
//    private PermissionsRepository permissionsRepository;
//
//    @InjectMocks
//    private PermissionServiceImpl permissionService;
//
//    @Test
//    void shouldUsePermissionsRepositoryAsDataSourceWhenIsAccessDeniedInvoked() {
//        //Given
//        var resource = new AskedResourceDto(POST.name(), "/test");
//        var userToken = "jwt-example";
//        when(permissionsRepository.getByUserToken(any())).thenReturn(Collections.emptyList());
//
//        //When
//        var response = permissionService.isAccessDenied(resource, userToken);
//
//        //Then
//        verify(permissionsRepository, times(1)).getByUserToken(userToken);
//    }
//
//    @Test
//    void shouldDenyAccessWhenPermittedResourcesIsEmpty() {
//        //Given
//        var resource = new AskedResourceDto(POST.name(), "/test");
//        var userToken = "jwt-example";
//
//        List<Resource> permittedResources = Collections.emptyList();
//        when(permissionsRepository.getByUserToken(any())).thenReturn(permittedResources);
//
//        //When
//        var result = permissionService.isAccessDenied(resource, userToken);
//
//        //Then
//        assertThat(result).isTrue();
//    }
//
//    @Test
//    void shouldDenyAccessWhenUserResourcesHaveNoMatchesByName() {
//        //Given
//        var askedResource = new AskedResourceDto(POST.name(), "/test");
//        var userToken = "jwt-example";
//
//        var permitted = List.of(
//                Resource.builder().name("not a test").build()
//        );
//        when(permissionsRepository.getByUserToken(any())).thenReturn(permitted);
//
//        //When
//        var result = permissionService.isAccessDenied(askedResource, userToken);
//
//        //Then
//        assertThat(result).isTrue();
//    }
//
//    @Test
//    void shouldDenyAccessWhenUserResourcesHaveMatchesByNameButDifferentScope() {
//        //Given
//        var askedResourceName = "/test";
//        var askedResource = new AskedResourceDto(POST.name(), askedResourceName);
//        var userToken = "jwt-example";
//
//        var permitted = List.of(
//                Resource.builder()
//                        .name(askedResourceName)
//                        .scopes(Set.of("Read"))
//                        .build()
//        );
//        when(permissionsRepository.getByUserToken(any())).thenReturn(permitted);
//
//        //When
//        var result = permissionService.isAccessDenied(askedResource, userToken);
//
//        //Then
//        assertThat(result).isTrue();
//    }
//
//    @Test
//    void shouldAllowAccessWhenUserResourcesMatchAskedResourceNameAndScope() {
//        //Given
//        var askedResourceName = "/test";
//        var askedResource = new AskedResourceDto(POST.name(), askedResourceName);
//        var userToken = "jwt-example";
//
//        var permitted = List.of(
//                Resource.builder()
//                        .name(askedResourceName)
//                        .scopes(Set.of("Read"))
//                        .build()
//        );
//        when(permissionsRepository.getByUserToken(any())).thenReturn(permitted);
//
//        //When
//        var result = permissionService.isAccessDenied(askedResource, userToken);
//
//        //Then
//        assertThat(result).isFalse();
//    }
//}