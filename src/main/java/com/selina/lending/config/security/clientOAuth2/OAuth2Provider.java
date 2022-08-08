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

package com.selina.lending.config.security.clientOAuth2;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Provider {

    // Using anonymous user principal as its Service-to-Service authentication
    private static final Authentication ANONYMOUS_USER_AUTHENTICATION = new AnonymousAuthenticationToken(
            "key",
            "anonymous",
            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
    );

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public OAuth2Provider(OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    public String getAuthenticationToken(final String authServerName) {
        var request = buildOAuth2AuthorizeRequest(authServerName);
        return "Bearer " + authorizedClientManager.authorize(request).getAccessToken().getTokenValue();
    }

    private static OAuth2AuthorizeRequest buildOAuth2AuthorizeRequest(String authServerName) {
        return OAuth2AuthorizeRequest.withClientRegistrationId(authServerName)
                .principal(ANONYMOUS_USER_AUTHENTICATION)
                .build();
    }
}
