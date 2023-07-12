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

package com.selina.lending.repository;

import com.selina.lending.httpclient.middleware.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.httpclient.middleware.dto.dip.request.ApplicationRequest;
import com.selina.lending.httpclient.middleware.dto.dip.response.ApplicationResponse;
import com.selina.lending.httpclient.middleware.dto.product.response.SelectProductResponse;
import com.selina.lending.httpclient.middleware.dto.qq.request.QuickQuoteRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.request.QuickQuoteCFRequest;
import com.selina.lending.httpclient.middleware.dto.qqcf.response.QuickQuoteCFResponse;
import org.springframework.core.io.Resource;

import java.util.Optional;

public interface MiddlewareRepository {

    Optional<ApplicationDecisionResponse> getApplicationById(String id);

    Resource downloadEsisDocByAppId(String id);

    ApplicationResponse createDipCCApplication(ApplicationRequest applicationRequest);

    ApplicationResponse createDipApplication(ApplicationRequest applicationRequest);

    QuickQuoteCFResponse createQuickQuoteCFApplication(QuickQuoteCFRequest applicationRequest);

    SelectProductResponse selectProduct(String id, String productCode);

    void patchDipApplication(String id, ApplicationRequest applicationRequest);

    void patchDipCCApplication(String id, ApplicationRequest applicationRequest);

    ApplicationResponse checkAffordability(String id);

    void createQuickQuoteApplication(QuickQuoteRequest applicationRequest);
}

