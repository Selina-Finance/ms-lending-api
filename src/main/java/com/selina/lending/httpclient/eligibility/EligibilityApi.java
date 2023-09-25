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

package com.selina.lending.httpclient.eligibility;

import com.selina.lending.httpclient.eligibility.dto.request.EligibilityRequest;
import com.selina.lending.httpclient.eligibility.dto.response.EligibilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
        value = "eligibility-api",
        url = "${service.ms-eligibility.url}"
)
public interface EligibilityApi {

    @PostMapping(path = "/v1/eligibility", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    EligibilityResponse getEligibility(EligibilityRequest eligibilityRequest);
}
