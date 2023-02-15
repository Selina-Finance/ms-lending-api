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

package com.selina.lending.internal.dto;

import static com.selina.lending.api.controller.SwaggerConstants.DATE_TIME_FORMAT;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreditCheckDto {
    String serviceUsed;
    String requestId;
    String requestTimeStamp;
    String responseId;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    String responseTimeStamp;
    String responseStatus;
    Integer creditScore;
    String creditCheckReference;
    String errorCode;
    String message;
    String status;
    Boolean hardCheckCompleted;
    String hardCheckCompletedDate;
}
