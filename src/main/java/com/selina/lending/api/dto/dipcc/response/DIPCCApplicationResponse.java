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

package com.selina.lending.api.dto.dipcc.response;

import com.selina.lending.api.dto.creditcommitments.response.CreditCommitmentDto;
import com.selina.lending.api.dto.dip.response.ApplicationDto;
import com.selina.lending.api.dto.dip.response.DIPApplicationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class DIPCCApplicationResponse {
    String requestType;
    String applicationId;
    @Schema (oneOf = DIPApplicationDto.class)
    ApplicationDto application;
    CreditCommitmentDto creditCommitment;
}
