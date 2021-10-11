package com.softserve.teachua.dto.club.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // enable addition @annotation to class
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DescriptionValidation.class) // class to process logic: valid / not valid

public @interface ClubDescription {
    String message() default "Invalid description";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
