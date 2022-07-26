package com.selina.lending.api.errors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.Optional;

import static com.selina.lending.api.errors.ErrorConstants.UNABLE_TO_CONVERT_HTTP_MESSAGE;
import static com.selina.lending.api.errors.ErrorConstants.UNEXPECTED_RUNTIME_EXCEPTION;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

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
                .with("violations", ((ConstraintViolationProblem) problem).getViolations());
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ProblemBuilder prepare(final Throwable throwable, final StatusType status, final URI type) {
        if (isHttpMessageConversionException(throwable)) {
            return buildProblem(status, UNABLE_TO_CONVERT_HTTP_MESSAGE, throwable);
        }

        if (isNeedToHideFrameworkDetails(throwable.getMessage())) {
            return buildProblem(status, UNEXPECTED_RUNTIME_EXCEPTION, throwable);
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
