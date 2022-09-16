package com.softserve.teachua.utils.annotation;

import com.softserve.teachua.constants.RoleData;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface AllowedRoles {
    RoleData[] value();
}
