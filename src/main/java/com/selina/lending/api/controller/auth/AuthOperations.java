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

package com.selina.lending.api.controller.auth;

import com.selina.lending.internal.dto.auth.TokenResponse;
import com.selina.lending.internal.dto.auth.Credentials;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static com.selina.lending.api.controller.SwaggerConstants.INVALID_CREDENTIALS_EXAMPLE;

public interface AuthOperations {


    @Operation(description = "Create a new authentication token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication token and amount of seconds before it will be expired",
                    content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TokenResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid client credentials",
                    content = @Content (examples = {@ExampleObject(value = INVALID_CREDENTIALS_EXAMPLE)})
            ),
    })
    @PostMapping(value = "/auth/token")
    ResponseEntity<TokenResponse> createToken(@Valid @RequestBody Credentials credentials);
}
