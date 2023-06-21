package com.selina.lending.api.support.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = UkNationalPhoneNumberImpl.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UkNationalPhoneNumber {

    String message() default "must be a valid GB phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
