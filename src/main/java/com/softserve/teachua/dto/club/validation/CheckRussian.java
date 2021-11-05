package com.softserve.teachua.dto.club.validation;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


   /**
    *   Use this annotation to check  field on containing Russian letters like -ёЁыЫъЪэЭ
    *   Allowed Ukrainian,English alphabet and special symbols
    *
    */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotRussianValidator.class)
public @interface CheckRussian {

    String message() default "Text can contain only English and Ukrainian letter and special symbols";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
