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
//package com.selina.lending.internal.service.permissions.annotation;
//
//import com.selina.lending.internal.dto.permissions.AskedResourceDto;
//import com.selina.lending.internal.repository.auth.PermissionsRepository;
//import com.selina.lending.internal.service.permissions.PermissionServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.web.bind.annotation.RequestMethod.POST;
//
//@ExtendWith(MockitoExtension.class)
//class PermissionAspectTest {
//
//    @Mock
//    private PermissionsRepository repository;
//    @Mock
//    private PermissionServiceImpl service;
//
//    @InjectMocks
//    private PermissionAspect aspect;
//
//
//    @Test
//    void shouldContinueProcessingWhenServiceAllowedAccess() {
//        //Given
//        //When
//
//        //Then
//    }
//
//    @Test
//    void shouldThrowAccessDeniedWhenServiceDeniedTheAccessToTheResource() {
//        //Given
//
//        //When
//
//        //Then
//    }
//
//}