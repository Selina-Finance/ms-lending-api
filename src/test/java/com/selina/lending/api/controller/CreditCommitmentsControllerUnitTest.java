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
//package com.selina.lending.api.controller;
//
//import com.selina.lending.internal.dto.ApplicationResponse;
//import com.selina.lending.internal.dto.сreditCommitments.UpdateCreditCommitmentsRequest;
//import com.selina.lending.internal.service.creaditCommitments.UpdateCreditCommitmentsService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class CreditCommitmentsControllerUnitTest {
//
//    @Mock
//    private UpdateCreditCommitmentsService service;
//
//    @InjectMocks
//    private CreditCommitmentsController controller;
//
//    @Test
//    void whenUpdateCreditCommitmentsThenCallUpdateCCService() {
//        //Given
//        var externalId = UUID.randomUUID().toString();
//        var request = new UpdateCreditCommitmentsRequest();
//
//        var response = ApplicationResponse.builder().build();
//        when(service.patchCreditCommitments(externalId, request)).thenReturn(response);
//
//        //When
//        var appResponse = controller.updateCreditCommitments(externalId, request);
//
//        //Then
//        verify(updateApplicationService, times(1)).updateDipApplication(eq(APPLICATION_ID), any());
//    }
//}