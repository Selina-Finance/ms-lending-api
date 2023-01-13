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

package com.selina.lending.internal.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selina.lending.api.errors.custom.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class AccessManagementServiceTest {

    private static final String SOURCE_ACCOUNT = "account";
    private static final String ANOTHER_ACCOUNT = "another account";

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AccessManagementService accessManagementService;

    @Test
    void checkSourceAccountAccessPermitted() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        accessManagementService.checkSourceAccountAccessPermitted(SOURCE_ACCOUNT);

        //Then
        verify(tokenService, times(1)).retrieveSourceAccount();
    }

    @Test
    void checkSourceAccountAccessPermittedThrowsAccessDeniedException() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(ANOTHER_ACCOUNT);

        //When
        var exception = assertThrows(AccessDeniedException.class, () -> accessManagementService.checkSourceAccountAccessPermitted(
                SOURCE_ACCOUNT));

        //Then
        assertThat(exception.getMessage(), equalTo("Error processing request: Access denied for application"));
        verify(tokenService, times(1)).retrieveSourceAccount();
    }

    @Test
    void isSourceAccountAccessAllowedWhenSourceAccountMatchesReturnsTrue() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        boolean accessAllowed = accessManagementService.isSourceAccountAccessAllowed(SOURCE_ACCOUNT);

        //Then
        assertThat(accessAllowed, equalTo(true));
    }

    @Test
    void isSourceAccountAccessAllowedWhenSourceAccountUnMatchedReturnsFalse() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(SOURCE_ACCOUNT);

        //When
        boolean accessAllowed = accessManagementService.isSourceAccountAccessAllowed(ANOTHER_ACCOUNT);

        //Then
        assertThat(accessAllowed, equalTo(false));
    }

    @Test
    void isSourceAccountAccessAllowedWhenTokenServiceSourceAccountNullReturnsFalse() {
        //Given
        when(tokenService.retrieveSourceAccount()).thenReturn(null);

        //When
        boolean accessAllowed = accessManagementService.isSourceAccountAccessAllowed(SOURCE_ACCOUNT);

        //Then
        assertThat(accessAllowed, equalTo(false));
    }
}