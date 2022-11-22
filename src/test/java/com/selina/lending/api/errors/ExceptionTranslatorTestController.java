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

package com.selina.lending.api.errors;

import java.util.HashMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.selina.lending.api.errors.custom.RemoteResourceProblemException;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;

@RestController
@RequestMapping("/api/exception-translator-test")
public class ExceptionTranslatorTestController {

    @PostMapping("/method-argument")
    public void methodArgument(@Valid @RequestBody ExceptionTranslatorTestController.TestDto testDTO) {
    }

    @GetMapping("/missing-servlet-request-part")
    public void missingServletRequestPartException(@RequestPart String part) {
    }

    @GetMapping("/missing-servlet-request-parameter")
    public void missingServletRequestParameterException(@RequestParam String param) {
    }

    @GetMapping("/access-denied")
    public void accessdenied() {
        throw new AccessDeniedException("test access denied!");
    }

    @GetMapping("/unauthorized")
    public void unauthorized() {
        throw new BadCredentialsException("test authentication failed!");
    }

    @GetMapping("/response-status")
    public void exceptionWithResponseStatus() {
        throw new TestResponseStatusException();
    }

    @GetMapping("/internal-server-error")
    public void internalServerError() {
        throw new RuntimeException();
    }

    @GetMapping("/access-denied-exception")
    public void accessDeniedException() {
        throw new com.selina.lending.api.errors.custom.AccessDeniedException("Some problem details that make sense");
    }

    @GetMapping("/bad-request-exception")
    public void badRequestException() {
        throw new com.selina.lending.api.errors.custom.BadRequestException("bad request");
    }

    @GetMapping("/custom-remote-resource-problem-exception")
    public void remoteResourceProblemException() {
        throw new RemoteResourceProblemException();
    }

    @GetMapping("/feign-bad-request-exception")
    public void feignBadRequestException() {
        var request = Request.create(Request.HttpMethod.GET, "/feign-bad-request-exception",
                new HashMap<>(), null, new RequestTemplate());
        throw new FeignException.BadRequest("Bad request", request, "bad request".getBytes(), null);
    }

    @GetMapping("/feign-not-found-exception")
    public void feignNotFoundException() {
        var request = Request.create(Request.HttpMethod.GET, "/feign-not-found-exception",
                new HashMap<>(), null, new RequestTemplate());
        throw new FeignException.NotFound("Not Found", request, "not found".getBytes(), null);
    }

    public static class TestDto {

        @NotNull
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "test response status")
    @SuppressWarnings("serial")
    public static class TestResponseStatusException extends RuntimeException {
    }
}
