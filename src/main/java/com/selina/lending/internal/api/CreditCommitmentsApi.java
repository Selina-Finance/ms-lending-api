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

import com.selina.lending.config.security.clientOAuth2.MiddlewareOAuth2Configuration;
import com.selina.lending.internal.dto.creditCommitments.UpdateCreditCommitmentsRequest;
import com.selina.lending.internal.dto.creditCommitments.CreditCommitmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
        value = "credit-commitments-api",
        url = "${middleware.credit-commitments.url}",
        configuration = MiddlewareOAuth2Configuration.class
)
public interface CreditCommitmentsApi {

    @PatchMapping(path = "/creditcommitments/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    CreditCommitmentResponse patchCreditCommitments(@PathVariable("id") String id, @RequestBody UpdateCreditCommitmentsRequest request);
}
