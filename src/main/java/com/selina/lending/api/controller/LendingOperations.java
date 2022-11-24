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

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.selina.lending.internal.dto.ApplicationDecisionResponse;
import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.dto.DIPApplicationRequest;
import com.selina.lending.internal.dto.SelectProductResponse;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@OpenAPIDefinition(info = @Info(title = "Lending API", description = "Lending API service", license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")))
@RequestMapping("/application")
public interface LendingOperations {
    @Operation(description = "Retrieve the application for the given externalApplicationId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found application",
                    content = {
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ApplicationDecisionResponse.class))
                    }),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "404", description = "Application not found", content = @Content)
    })
    @GetMapping(value = "/{externalApplicationId}")
    ResponseEntity<ApplicationDecisionResponse> getApplication(@Parameter(description = "externalApplicationId of application to be searched", required = true) @PathVariable String externalApplicationId);

    @Operation(description = "Update the Decision In Principle (DIP) application for the given external application id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Application updated",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApplicationResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400", description = "Application details invalid", content = @Content),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403", content = @Content),
            @ApiResponse(responseCode = "404", description = "Application not found", content = @Content),
    })
    @PutMapping(value = "/{externalApplicationId}/dip")
    ResponseEntity<ApplicationResponse> updateDipApplication(@Parameter(description = "externalApplicationId of application to be updated", required = true) @PathVariable String externalApplicationId,
            @Valid @RequestBody DIPApplicationRequest dipApplicationRequest);

    @Operation(description = "Create a new Decision In Principle (DIP) application")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Application created",
                    content = {
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ApplicationResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Application details invalid", content = @Content),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403", content = @Content)
    })
    @PostMapping(value = "/dip")
    ResponseEntity<ApplicationResponse> createDipApplication(@Valid @RequestBody DIPApplicationRequest dipApplicationRequest);



    @Hidden
    @Operation(description = "Select a product offer for the Decision In Principle (DIP) application")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product offer selected",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SelectProductResponse.class))
                    }),
           @ApiResponse(responseCode = "400", description = "externalApplicationId or productCode invalid", content = @Content),
           @ApiResponse(responseCode = "401", content = @Content),
           @ApiResponse(responseCode = "403", content = @Content),
           @ApiResponse(responseCode = "404", description = "Application not found", content = @Content)
    })
    @PutMapping(value = "/{externalApplicationId}/product/{productCode}")
    ResponseEntity <SelectProductResponse> selectProductOffer(@Parameter(description = "externalApplicationId of application", required = true) @PathVariable String externalApplicationId,
    @Parameter(description = "productCode of the product offer to select", required = true) @PathVariable String productCode);
}
