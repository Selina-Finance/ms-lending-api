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

package com.selina.lending.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BarAuthZConfiguration {

    private final OAuth2Provider oauth2Provider;
    private final String AUTHZ_SERVER_NAME = "bar-auth";

    @Bean
    public RequestInterceptor barAuthZInterceptor() {
        return (requestTemplate) ->
                requestTemplate.header(
                        HttpHeaders.AUTHORIZATION, oauth2Provider.getAuthenticationToken(AUTHZ_SERVER_NAME));
    }
}
