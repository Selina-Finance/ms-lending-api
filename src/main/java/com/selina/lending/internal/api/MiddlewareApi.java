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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFRequest;
import com.selina.lending.internal.service.application.domain.quotecf.QuickQuoteCFResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.selina.lending.config.security.clientOAuth2.MiddlewareOAuth2Configuration;
import com.selina.lending.internal.service.application.domain.ApplicationDecisionResponse;
import com.selina.lending.internal.service.application.domain.ApplicationRequest;
import com.selina.lending.internal.service.application.domain.ApplicationResponse;
import com.selina.lending.internal.service.application.domain.SelectProductResponse;

@FeignClient(
        value = "middleware-api",
        url = "${middleware.api.url}",
        configuration = MiddlewareOAuth2Configuration.class
)
public interface MiddlewareApi {

    @GetMapping(path = "/application/{id}", produces = APPLICATION_JSON_VALUE)
    ApplicationDecisionResponse getApplicationById(@PathVariable("id") String id);

    @PostMapping(path = "/application/dip", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ApplicationResponse createDipCCApplication(ApplicationRequest applicationRequest);

    @PostMapping(path = "/application/light_decision", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ApplicationResponse createDipApplication(ApplicationRequest applicationRequest);

    @PostMapping(path = "/quote_broker", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    QuickQuoteCFResponse createQuickQuoteCFApplication(QuickQuoteCFRequest applicationRequest);

    @PutMapping(path = "/application/{id}/selectProduct/{productCode}")
    SelectProductResponse selectProduct(@PathVariable("id") String id, @PathVariable("productCode") String productCode);

    @GetMapping(value = "/application/{id}/esis-document", produces = APPLICATION_PDF_VALUE)
    Resource downloadEsisByAppId(@PathVariable String id);

    @PatchMapping(value = "/application/{id}", consumes = APPLICATION_JSON_VALUE)
    void patchApplication(@PathVariable("id") String id, ApplicationRequest applicationRequest);

    @GetMapping(path = "/application/{id}/checkAffordability", produces = APPLICATION_JSON_VALUE)
    ApplicationResponse checkAffordability(@PathVariable("id") String id);
}
