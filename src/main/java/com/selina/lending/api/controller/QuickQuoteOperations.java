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

import static com.selina.lending.api.controller.SwaggerConstants.ACCESS_DENIED_EXAMPLE;
import static com.selina.lending.api.controller.SwaggerConstants.BAD_REQUEST_EXAMPLE;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.selina.lending.internal.dto.quote.QuickQuoteApplicationRequest;
import com.selina.lending.internal.dto.quote.QuickQuoteResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping("/application")
public interface QuickQuoteOperations {


    @Operation(description = "Create a new Quick Quote application")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quick Quote created",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = QuickQuoteResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Application details invalid",
                    content = @Content (examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403",
                    content = @Content (examples = {@ExampleObject(value = ACCESS_DENIED_EXAMPLE)})
            )
    })
    @PostMapping(value = "/quickquote")
    ResponseEntity<QuickQuoteResponse> createQuickQuoteApplication(@Valid @RequestBody
    QuickQuoteApplicationRequest quickQuoteApplicationRequest);


    @Operation(description = "Create a new Quick Quote CC application")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quick Quote CC created",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = QuickQuoteResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Application details invalid",
                    content = @Content (examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403",
                    content = @Content (examples = {@ExampleObject(value = ACCESS_DENIED_EXAMPLE)})
            )
    })
    @PostMapping(value = "/quickquotecc")
    ResponseEntity<QuickQuoteResponse> createQuickQuoteCCApplication(@Valid @RequestBody
                                                                   QuickQuoteApplicationRequest quickQuoteApplicationRequest);




    @Operation(description = "Update the Quick Quote application for the given external application id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quick Quote updated",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = QuickQuoteResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400", description = "Application details invalid",
                    content = @Content (examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403",
                    content = @Content (examples = {@ExampleObject(value = ACCESS_DENIED_EXAMPLE)})
            ),
    })
    @PutMapping(value = "/{externalApplicationId}/quickquote")
    ResponseEntity<QuickQuoteResponse> updateQuickQuoteApplication(@Parameter(description = "externalApplicationId of application to be updated", required = true) @PathVariable
    String externalApplicationId,
            @Valid @RequestBody QuickQuoteApplicationRequest quickQuoteApplicationRequest);
}
