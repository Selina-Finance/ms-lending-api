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

package com.selina.lending.httpclient.middleware.dto.common;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class CreditCheck {
    private String serviceUsed;
    private String requestId;
    private String requestTimeStamp;
    private String responseId;
    private String responseTimeStamp;
    private String responseStatus;
    private Integer creditScore;
    private String creditCheckReference;
    private String errorCode;
    private String message;
    private String status;
    private Boolean hardCheckCompleted;
    private String hardCheckCompletedDate;
}
