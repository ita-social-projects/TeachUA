package com.softserve.teachua.utils.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *   Use this annotation to field that can't contain foreign language symbols
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckForeignLanguageValidator.class)
public @interface CheckForeignLanguage {
    //String message() default "Can't contain foreign language symbols except english";
    String message() default "Помилка. Текст містить недопустимі символи";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
