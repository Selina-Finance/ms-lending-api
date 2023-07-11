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

package com.selina.lending.httpclient.getapplication;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.selina.lending.httpclient.getapplication.dto.response.ApplicationIdentifier;

@FeignClient(
        value = "middleware-application-service-api",
        url = "${middleware.application.service.url}")
public interface GetApplicationApi {

    @GetMapping(path = "/application/application-id/{externalApplicationId}", produces = APPLICATION_JSON_VALUE)
    ApplicationIdentifier getApplicationIdByExternalApplicationId(@PathVariable("externalApplicationId") String externalApplicationId);

    @DeleteMapping(path = "/application/external-id/{externalApplicationId}")
    void deleteApplicationByExternalApplicationId(@RequestHeader("x-source-account") String sourceAccount, @PathVariable("externalApplicationId") String externalApplicationId);
}