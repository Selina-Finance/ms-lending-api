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

package com.selina.lending.api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.selina.lending.internal.dto.SelectProductResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping("/application")
public interface ProductOperations {

    @Operation(description = "Select a product offer for the Decision In Principle (DIP) application")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product offer selected",
                    content = {
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = SelectProductResponse.class)
                        )
                    }),
            @ApiResponse(
                    responseCode = "400", description = "externalApplicationId or productCode invalid", content = @Content
            ),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403", content = @Content),
            @ApiResponse(responseCode = "404", description = "Application not found", content = @Content)})
    @PutMapping(value = "/{externalApplicationId}/product/{productCode}")
    ResponseEntity<SelectProductResponse> selectProductOffer(
            @Parameter(description = "externalApplicationId of application", required = true) @PathVariable String externalApplicationId,
            @Parameter(description = "productCode of the product offer to select", required = true) @PathVariable String productCode);
}