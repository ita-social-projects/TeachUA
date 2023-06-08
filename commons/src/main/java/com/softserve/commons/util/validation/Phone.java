package com.softserve.commons.util.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidation.class)
public @interface Phone {
    String message() default "Invalid phone number. It should be 10 digits length and contain no letters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
