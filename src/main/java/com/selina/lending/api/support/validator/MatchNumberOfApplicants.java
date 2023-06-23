package com.selina.lending.api.support.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = MatchNumberOfApplicantsImpl.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchNumberOfApplicants {

    String message() default "should be equal to applicants size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
