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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.selina.lending.internal.dto.ApplicationResponse;
import com.selina.lending.internal.dto.creditcommitments.request.UpdateCreditCommitmentsRequest;
import com.selina.lending.internal.dto.creditcommitments.response.CreditCommitmentResponse;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Hidden
@RequestMapping("/application")
public interface CreditCommitmentsOperations {

    @Operation(description = "Update an application's Credit Commitments for the given external application id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Offers based on updated Credit Commitments",
                    content = {@Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationResponse.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Credit Commitments request is invalid", content = @Content),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403", content = @Content),
            @ApiResponse(responseCode = "404", content = @Content),
    })
    @PatchMapping(value = "/{externalApplicationId}/creditcommitments")
    ResponseEntity<ApplicationResponse> updateCreditCommitments(
            @Parameter(description = "externalApplicationId of application to be updated", required = true) @PathVariable String externalApplicationId,
            @Valid @RequestBody UpdateCreditCommitmentsRequest request
    );

    @Operation(description = "Download an application's ESIS pdf document")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ESIS pdf document for the application",
                    content = {@Content(mediaType = APPLICATION_PDF_VALUE)}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request is invalid",
                    content = @Content),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403", content = @Content),
            @ApiResponse(responseCode = "404", content = @Content),
    })
    @GetMapping(value = "/{externalApplicationId}/esis", produces = APPLICATION_PDF_VALUE)
    ResponseEntity<Resource> downloadEsis(
            @Parameter(description = "externalApplicationId of the application related to the ESIS document", required = true) @PathVariable String externalApplicationId
    ) throws IOException;



    @Operation(description = "Retrieve the Credit Commitments for the given external application id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the Credit Commitments for the application",
                    content = {@Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreditCommitmentResponse.class))}),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403", content = @Content),
            @ApiResponse(responseCode = "404", content = @Content),
    })
    @GetMapping(value = "/{externalApplicationId}/creditcommitments")
    ResponseEntity<CreditCommitmentResponse> getCreditCommitments(
            @Parameter(description = "externalApplicationId of application", required = true) @PathVariable String externalApplicationId);
}
