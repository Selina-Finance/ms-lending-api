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

package com.selina.lending.httpclient.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.selina.lending.httpclient.keycloak.dto.response.AuthApiTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
        value = "auth-api",
        url = "${spring.security.oauth2.resourceserver.jwt.issuer-uri}"
)
public interface KeycloakApi {

    @PostMapping(path = "/protocol/openid-connect/token",
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_JSON_VALUE)
    AuthApiTokenResponse login(@RequestBody Map<String, ?> params);

    record ErrorDetails(@JsonProperty("error") String error, @JsonProperty("error_description") String description){}
}
