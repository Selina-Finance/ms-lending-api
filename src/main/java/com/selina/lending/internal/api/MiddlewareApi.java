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

package com.selina.lending.internal.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.selina.lending.config.security.clientOAuth2.MiddlewareOAuth2Configuration;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;

@FeignClient(
        value = "middleware-api",
        url = "${middleware.api.url}",
        configuration = MiddlewareOAuth2Configuration.class
)
public interface MiddlewareApi {

    @GetMapping(path = "/application/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApplicationDecisionResponse getApplicationById(@PathVariable("id") String id);

    @PostMapping (path = "/application/dip", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ApplicationResponse createDipApplication(ApplicationRequest applicationRequest);

    @PutMapping(path = "/application/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateDipApplication(@PathVariable("id") String id, ApplicationRequest applicationRequest);
}
