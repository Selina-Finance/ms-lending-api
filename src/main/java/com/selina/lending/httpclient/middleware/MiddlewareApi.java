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

package com.selina.lending.httpclient.middleware;

import com.selina.lending.config.security.clientOAuth2.MiddlewareOAuth2Configuration;
import com.selina.lending.httpclient.middleware.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import com.selina.lending.httpclient.middleware.dto.dip.response.ApplicationResponse;
import com.selina.lending.httpclient.middleware.dto.product.response.SelectProductResponse;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.response.QuickQuoteCFResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@FeignClient(
        value = "middleware-api",
        url = "${middleware.api.url}",
        configuration = MiddlewareOAuth2Configuration.class
)
public interface MiddlewareApi {

    @GetMapping(path = "/application/{id}", produces = APPLICATION_JSON_VALUE)
    ApplicationDecisionResponse getApplicationById(@PathVariable("id") String id);

    @PostMapping(path = "/application/light_decision", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ApplicationResponse createDipApplication(ApplicationRequest applicationRequest);

    @PatchMapping(value = "/application/light/{id}", consumes = APPLICATION_JSON_VALUE)
    void patchDipApplication(@PathVariable("id") String id, ApplicationRequest applicationRequest);

    @PostMapping(path = "/application/dip", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ApplicationResponse createDipCCApplication(ApplicationRequest applicationRequest);

    @PatchMapping(value = "/application/{id}", consumes = APPLICATION_JSON_VALUE)
    void patchDipCCApplication(@PathVariable("id") String id, ApplicationRequest applicationRequest);

    @PostMapping(path = "/quote_broker", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    QuickQuoteCFResponse createQuickQuoteCFApplication(QuickQuoteCFRequest applicationRequest);

    @PutMapping(path = "/application/{id}/selectProduct/{productCode}")
    SelectProductResponse selectProduct(@PathVariable("id") String id, @PathVariable("productCode") String productCode);

    @GetMapping(value = "/application/{id}/esis-document", produces = APPLICATION_PDF_VALUE)
    Resource downloadEsisByAppId(@PathVariable String id);

    @GetMapping(path = "/application/{id}/checkAffordability", produces = APPLICATION_JSON_VALUE)
    ApplicationResponse checkAffordability(@PathVariable("id") String id);
}
