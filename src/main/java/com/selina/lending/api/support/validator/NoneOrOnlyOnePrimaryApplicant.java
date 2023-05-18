package com.selina.lending.api.support.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = NoneOrOnlyOnePrimaryApplicantImpl.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoneOrOnlyOnePrimaryApplicant {

    String message() default "must have one primary applicant";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
