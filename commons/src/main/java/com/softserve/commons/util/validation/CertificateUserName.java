package com.softserve.commons.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to check userName field on valid length, and valid words.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CertificateUserNameValidator.class)
public @interface CertificateUserName {
    String message() default "Неможливо розпізнати ім'я та прізвище";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
