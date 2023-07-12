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

package com.selina.lending.service;

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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {
    private static final String CLIENT_ID_VALUE = "the client id";
    private static final String SOURCE_ACCOUNT_VALUE = "source account";
    private static final String SOURCE_TYPE_VALUE = "source type";
    private static final Double ARRANGEMENT_FEE_DISCOUNT_SELINA_VALUE = 1.0;

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

    @Test
    void shouldReturnSourceType() {
        //Given
        mockSecurity(LendingConstants.SOURCE_TYPE_JWT_CLAIM_NAME, SOURCE_TYPE_VALUE);

        //When
        var value = tokenService.retrieveSourceType();

        //Then
        assertThat(value, equalTo(SOURCE_TYPE_VALUE));
    }

    @Test
    void shouldReturnArrangementFeeDiscountSelina() {
        //Given
        mockSecurity(LendingConstants.ARRANGEMENT_FEE_DISCOUNT_SELINA_JWT_CLAIM_NAME, "1.0");

        //When
        var value = tokenService.retrieveArrangementFeeDiscountSelina();

        //Then
        assertThat(value, equalTo(ARRANGEMENT_FEE_DISCOUNT_SELINA_VALUE));
    }

    @Test
    void whenTokenClaimArrangementFeeDiscountSelinaIsNullThenArrangementFeeDiscountSelinaIsNull() {
        //Given
        mockSecurity(LendingConstants.ARRANGEMENT_FEE_DISCOUNT_SELINA_JWT_CLAIM_NAME, null);

        //When
        var value = tokenService.retrieveArrangementFeeDiscountSelina();

        //Then
        assertNull(value);
    }

    @Test
    void whenTokenClaimArrangementFeeDiscountSelinaIsNotNumberThenArrangementFeeDiscountSelinaIsNull() {
        //Given
        mockSecurity(LendingConstants.ARRANGEMENT_FEE_DISCOUNT_SELINA_JWT_CLAIM_NAME, "Not Number");

        //When
        var value = tokenService.retrieveArrangementFeeDiscountSelina();

        //Then
        assertNull(value);
    }

    private void mockSecurity(String claimName, Object claimValue) {
        var authentication = Mockito.mock(Authentication.class);
        var jwt = Jwt.withTokenValue("abc").header("", "").claim(claimName, claimValue).build();
        when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}