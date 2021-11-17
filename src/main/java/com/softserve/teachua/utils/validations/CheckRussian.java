package com.softserve.teachua.utils.validations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *   Use this annotation to field that can't contain Russian letters like ёЁыЫъЪэЭ
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckRussianValidator.class)
public @interface CheckRussian {

    String message() default "can't contain russian letters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
