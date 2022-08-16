package com.softserve.teachua.utils.test.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to prevent the use of null or empty strings in collections.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WithoutBlank {
    String message() default "Вказана колекція не може містити null.";
}
