package com.selina.lending.api.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = MoveOutAfterMoveInDateImpl.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MoveOutAfterMoveInDate {

    String message() default "toDate must exceed fromDate";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

