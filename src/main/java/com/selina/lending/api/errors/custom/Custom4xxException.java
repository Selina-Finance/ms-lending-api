package com.selina.lending.api.errors.custom;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import javax.annotation.Nullable;

public class Custom4xxException extends AbstractThrowableProblem {

    public Custom4xxException(@Nullable String details) {
        super(null, "The custom exception title", Status.BAD_REQUEST, details);
    }
}
