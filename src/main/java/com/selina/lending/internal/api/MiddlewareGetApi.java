/*
 *   Copyright 2022 Selina Finance
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package com.selina.lending.internal.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.selina.lending.internal.service.application.domain.ApplicationIdentifier;

@FeignClient(
        value = "middleware-get-api",
        url = "${middleware.get.api.url}")
public interface MiddlewareGetApi {
    @GetMapping(path = "/application/application-id/{externalApplicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApplicationIdentifier getApplicationIdByExternalApplicationId(@PathVariable("externalApplicationId") String externalApplicationId);

    @GetMapping(path = "/application/source-account/{externalApplicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApplicationIdentifier getApplicationSourceAccountByExternalApplicationId(@PathVariable("externalApplicationId") String externalApplicationId);
}