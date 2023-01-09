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
//package com.selina.lending.internal.service.permissions.helpers;
//
//import com.selina.lending.internal.dto.permissions.AskedResourceDto;
//import com.selina.lending.internal.service.application.domain.auth.authorization.Resource;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Set;
//
//import static com.selina.lending.internal.service.permissions.helpers.PermissionsCalculationHelper.isUserResourcesContainsAskedResource;
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//class PermissionsCalculationHelperTest {
//
//    @Test
//    void shouldTrueWhenAskedResourceIsInList() {
//        // Given
//        var resources = List.of(Resource.builder()
//                .name("DIP")
//                .scopes(Set.of("Write"))
//                .build());
//        var askedResource = new AskedResourceDto("POST", "/test");
//
//        String authTranslatedMethod = askedResource.authTranslatedMethod();
//
//        // When
//        var result = isUserResourcesContainsAskedResource(resources, askedResource);
//
//        // Then
//        assertThat(result).isTrue();
//    }
//}