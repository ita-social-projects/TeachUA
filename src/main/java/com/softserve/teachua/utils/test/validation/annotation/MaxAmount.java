package com.softserve.teachua.utils.test.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to set the maximum number of elements in a particular collection.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaxAmount {
    int max();
    String message() default "Кількість варіантів відповідей перевищує максимальну допустиму";
}
