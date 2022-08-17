/*
 *  Copyright 2022 Selina Finance
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.selina.lending.api.errors;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.common.HttpStatusAdapter;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.Optional;

import static com.selina.lending.api.errors.ErrorConstants.UNABLE_TO_CONVERT_HTTP_MESSAGE_DETAIL;
import static com.selina.lending.api.errors.ErrorConstants.UNEXPECTED_RUNTIME_EXCEPTION_DETAIL;
import static com.selina.lending.api.errors.ErrorConstants.VIOLATIONS_KEY;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    /**
     * Post-process the Problem payload to add/remove the message key for the front-end if needed.
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return null;
        }

        if (entity.getBody() instanceof ConstraintViolationProblem) {
            return mapConstraintViolationProblem(entity);
        }

        return entity;
    }

    private ResponseEntity<Problem> mapConstraintViolationProblem(ResponseEntity<Problem> entity) {
        Problem problem = entity.getBody();
        ProblemBuilder builder = Problem
                .builder()
                .withStatus(problem.getStatus())
                .withTitle(problem.getTitle())
                .with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations());
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ProblemBuilder prepare(final Throwable throwable, final StatusType status, final URI type) {
        if (throwable instanceof FeignException) {
            var feignException = (FeignException) throwable;
            return buildProblem(new HttpStatusAdapter(HttpStatus.valueOf(feignException.status())), feignException.contentUTF8(), feignException);
        }

        if (isHttpMessageConversionException(throwable)) {
            return buildProblem(status, UNABLE_TO_CONVERT_HTTP_MESSAGE_DETAIL, throwable);
        }

        if (isNeedToHideFrameworkDetails(throwable.getMessage())) {
            return buildProblem(status, UNEXPECTED_RUNTIME_EXCEPTION_DETAIL, throwable);
        }

        return buildProblem(status, throwable.getMessage(), throwable);
    }

    private ProblemBuilder buildProblem(StatusType status, String detail, Throwable throwable) {
        return Problem
                .builder()
                .withTitle(status.getReasonPhrase())
                .withStatus(status)
                .withDetail(detail)
                .withCause(
                        Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                );
    }

    private boolean isHttpMessageConversionException(Throwable throwable) {
        return throwable instanceof HttpMessageConversionException;
    }

    private boolean isNeedToHideFrameworkDetails(String message) {
        // This list is for sure not complete
        return StringUtils.containsAny(
                message,
                "com.fasterxml.", "org.", "java.", "net.", "javax.", "com.", "io.", "de."
        );
    }
}
