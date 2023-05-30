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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.selina.lending.internal.dto.LendingConstants;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String retrieveClientId() {
        return retrieveFromToken(LendingConstants.CLIENT_ID_JWT_CLAIM_NAME);
    }

    @Override
    public String retrieveSourceAccount() {
        return retrieveFromToken(LendingConstants.SOURCE_ACCOUNT_JWT_CLAIM_NAME);
    }

    @Override
    public String retrievePartnerAccountId() {
        try {
            return retrieveFromToken(LendingConstants.PARTNER_ACCOUNT_ID_JWT_CLAIM_NAME);
        } catch (NullPointerException exception) {
            // request is not coming from a partner so can safely ignore
            return null;
        }
    }

    @Override
    public String retrieveSourceType() {
        return retrieveFromToken(LendingConstants.SOURCE_TYPE_JWT_CLAIM_NAME);
    }

    @Override
    public Double retrieveArrangementFeeDiscountSelina() {
        try{
            return Double.parseDouble(retrieveFromToken(LendingConstants.ARRANGEMENT_FEE_DISCOUNT_SELINA_JWT_CLAIM_NAME));
        } catch (NullPointerException | NumberFormatException ex) {
            // Request is not coming from a broker or aggregator
            return null;
        }
    }

    private String retrieveFromToken(String property) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var jwt = (Jwt) authentication.getPrincipal();
        return (String) jwt.getClaims().get(property);
    }
}
