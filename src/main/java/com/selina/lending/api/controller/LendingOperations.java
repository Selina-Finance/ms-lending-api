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

package com.selina.lending.api.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.dto.DIPApplicationRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

public interface LendingOperations {
    @Operation(description = "Retrieve the application for the given application id")
    @GetMapping(value = "/{id}")
    ResponseEntity getApplication(@Parameter(name = "id", schema = @Schema(type = "String", example = "uniqueIdValue", description = "unique id for the application", required = true))
    @PathVariable String id);

    @GetMapping(value = "/test")
    String securityTest(@AuthenticationPrincipal Jwt principal, String id);

    @Operation(description = "Update the Decision In Principle (DIP) application for the given application id")
    @PutMapping(value = "/{id}/dip")
    ResponseEntity updateDipApplication(@Parameter(name = "id", schema = @Schema(type = "String", example = "uniqueIdValue", description = "unique id for the application", required = true))
    @PathVariable String id, @Valid @RequestBody DIPApplicationRequest dipApplicationRequest);

    @Operation(description = "Create a new Decision In Principle (DIP) application")
    @PostMapping(value = "/dip")
    ResponseEntity<ApplicationResponse> createDipApplication(@Valid @RequestBody DIPApplicationRequest dipApplicationRequest);
}
