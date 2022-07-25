package com.selina.lending.api.errors.custom;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import javax.annotation.Nullable;

public class MyCustomException extends AbstractThrowableProblem {

    public MyCustomException(@Nullable String details) {
        super(null, "This application does not exist", Status.INTERNAL_SERVER_ERROR, details);
    }
}
