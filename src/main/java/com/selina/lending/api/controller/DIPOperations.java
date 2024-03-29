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

import com.selina.lending.api.dto.application.response.ApplicationDecisionResponse;
import com.selina.lending.api.dto.dip.request.DIPApplicationRequest;
import com.selina.lending.api.dto.dip.response.DIPApplicationResponse;
import com.selina.lending.api.dto.dipcc.request.DIPCCApplicationRequest;
import com.selina.lending.api.dto.dipcc.response.DIPCCApplicationResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static com.selina.lending.api.controller.SwaggerConstants.ACCESS_DENIED_EXAMPLE;
import static com.selina.lending.api.controller.SwaggerConstants.BAD_REQUEST_EXAMPLE;
import static com.selina.lending.api.controller.SwaggerConstants.CONFLICT_EXAMPLE;
import static com.selina.lending.api.controller.SwaggerConstants.NOT_FOUND_EXAMPLE;

@OpenAPIDefinition(info = @Info(title = "Lending API", description = "Lending API service", license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")))
@RequestMapping("/application")
public interface DIPOperations {

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
            @ApiResponse(responseCode = "404", description = "Application not found",
                    content = @Content (examples = {@ExampleObject(value = NOT_FOUND_EXAMPLE)})
            )
    })
    @GetMapping(value = "/{externalApplicationId}")
    ResponseEntity<ApplicationDecisionResponse> getApplication(@Parameter(description = "externalApplicationId of application to be searched", required = true) @PathVariable String externalApplicationId);

    @Operation(description = "Update the Decision In Principle (DIP) application which has credit commitments for the given external application id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Application updated",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400", description = "Application details invalid",
                    content = @Content (examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "401",
                    content = @Content
            ),
            @ApiResponse(responseCode = "403",
                    content = @Content (examples = {@ExampleObject(value = ACCESS_DENIED_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "404", description = "Application not found",
                    content = @Content (examples = {@ExampleObject(value = NOT_FOUND_EXAMPLE)})
            )
    })
    @PutMapping(value = "/{externalApplicationId}/dipcc")
    ResponseEntity<Void> updateDipCCApplication(@Parameter(description = "externalApplicationId of application to be updated", required = true) @PathVariable String externalApplicationId,
                                                               @Valid @RequestBody DIPCCApplicationRequest request);

    @Operation(description = "Create a new Decision In Principle (DIP) with credit commitments application")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Application created",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DIPCCApplicationResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Application details invalid",
                    content = @Content (examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403",
                    content = @Content (examples = {@ExampleObject(value = ACCESS_DENIED_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "409",
                    content = @Content (examples = {@ExampleObject(value = CONFLICT_EXAMPLE)
            }))
    })
    @PostMapping(value = "/dipcc")
    ResponseEntity<DIPCCApplicationResponse> createDipCCApplication(@Valid @RequestBody DIPCCApplicationRequest request);

    @Operation(description = "Update the Decision In Principle (DIP) application for the given external application id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Application updated",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400", description = "Application details invalid",
                    content = @Content (examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403",
                    content = @Content (examples = {@ExampleObject(value = ACCESS_DENIED_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "404", description = "Application not found",
                    content = @Content (examples = {@ExampleObject(value = NOT_FOUND_EXAMPLE)})
            )
    })
    @PutMapping(value = "/{externalApplicationId}/dip")
    ResponseEntity<Void> updateDipApplication(@Parameter(description = "externalApplicationId of application to be updated", required = true) @PathVariable String externalApplicationId,
                                                             @Valid @RequestBody DIPApplicationRequest dipApplicationRequest);

    @Operation(description = "Create a new Decision In Principle (DIP) application")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Application created",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DIPApplicationResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Application details invalid",
                    content = @Content (examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "403",
                    content = @Content (examples = {@ExampleObject(value = ACCESS_DENIED_EXAMPLE)})
            ),
            @ApiResponse(responseCode = "409",
                    content = @Content (examples = {@ExampleObject(value = CONFLICT_EXAMPLE)
            }))
    })
    @PostMapping(value = "/dip")
    ResponseEntity<DIPApplicationResponse> createDipApplication(@Valid @RequestBody DIPApplicationRequest dipApplicationRequest);
}
