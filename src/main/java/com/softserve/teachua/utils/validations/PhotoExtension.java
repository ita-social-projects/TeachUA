package com.softserve.teachua.utils.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to check whether file is a photo of allowed formats
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhotoExtensionValidator.class)
public @interface PhotoExtension {
    String message() default "Неправильний формат файлу. Дозволені формати: .jpg, .jpeg та .png";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
