package com.softserve.commons.util.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Use this annotation to field that can't contain Russian letters like ёЁыЫъЪэЭ.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckRussianValidator.class)
public @interface CheckRussian {
    String message() default "Помилка. Присутні недопустимі символи";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
