package com.softserve.question.util.annotation;

import com.softserve.commons.constant.RoleData;
import com.softserve.commons.exception.UserPermissionException;
import com.softserve.question.security.UserPrincipal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AllowedRolesAspect {
    @Around("@annotation(com.softserve.question.util.annotation.AllowedRoles)")
    public Object doSomething(ProceedingJoinPoint jp) throws Throwable {
        try {
            Set<RoleData> roleData = Arrays
                    .stream(((MethodSignature) jp.getSignature()).getMethod().getAnnotation(AllowedRoles.class).value())
                    .collect(Collectors.toSet());
            UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            for (RoleData role : roleData) {
                if (user.getAuthorities().contains(new SimpleGrantedAuthority(role.getRoleName()))) {
                    return jp.proceed();
                }
            }
        } catch (ClassCastException e) {
            throw new UserPermissionException();
        }
        throw new UserPermissionException();
    }
}
