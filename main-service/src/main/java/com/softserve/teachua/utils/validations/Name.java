package com.softserve.teachua.utils.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Use this annotation to check firstname and lastname fields on valid length, containing valid letters. Allowed
 * ukrainian and english letters.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NameValidator.class)
public @interface Name {
    String message() default "Field can contain only ukrainian and english letters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
