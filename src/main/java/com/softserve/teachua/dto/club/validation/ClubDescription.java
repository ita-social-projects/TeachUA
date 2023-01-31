package com.softserve.teachua.dto.club.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DescriptionValidation.class)

public @interface ClubDescription {
    String message() default "Invalid description";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
