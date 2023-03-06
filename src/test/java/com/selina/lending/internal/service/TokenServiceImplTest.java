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

import com.selina.lending.internal.dto.LendingConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {
    private static final String CLIENT_ID_VALUE = "the client id";
    private static final String SOURCE_ACCOUNT_VALUE = "source account";

    private final TokenService tokenService = new TokenServiceImpl();

    @Test
    void retrieveClientIdReturnsClientIdValueSuccessfully() {
        //Given
        mockSecurity(LendingConstants.CLIENT_ID_JWT_CLAIM_NAME, CLIENT_ID_VALUE);

        //When
        var value = tokenService.retrieveClientId();

        //Then
        assertThat(value, equalTo(CLIENT_ID_VALUE));
    }

    @Test
    void retrieveSourceAccountReturnsSourceAccountSuccessfully() {
        //Given
        mockSecurity(LendingConstants.SOURCE_ACCOUNT_JWT_CLAIM_NAME, SOURCE_ACCOUNT_VALUE);

        //When
        var value = tokenService.retrieveSourceAccount();

        //Then
        assertThat(value, equalTo(SOURCE_ACCOUNT_VALUE));
    }

    private void mockSecurity(String claimName, String claimValue) {
        var authentication = Mockito.mock(Authentication.class);
        var jwt = Jwt.withTokenValue("abc").header("", "").claim(claimName, claimValue).build();
        when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}