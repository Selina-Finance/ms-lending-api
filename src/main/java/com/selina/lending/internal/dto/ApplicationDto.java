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

import static com.selina.lending.api.controller.swagger.SwaggerConstants.DATE_TIME_FORMAT;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public class ApplicationDto {
    private String decision;
    private String id;
    private String source;
    private String sourceClientId;
    private String sourceAccount;
    private String externalApplicationId;
    private String productCode;
    private String requestType;
    private String status;
    private String applicationStage;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private Date statusDate;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private Date createdDate;
}
