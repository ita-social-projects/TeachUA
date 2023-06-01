package com.softserve.teachua.utils.annotation;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.exception.UserPermissionException;
import com.softserve.teachua.security.CustomUserDetailsService;
import com.softserve.teachua.security.UserPrincipal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AllowedRolesAspect {
    private final CustomUserDetailsService customUserDetailsService;

    public AllowedRolesAspect(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Around("@annotation(com.softserve.teachua.utils.annotation.AllowedRoles)")
    public Object doSomething(ProceedingJoinPoint jp) throws Throwable {
        Set<RoleData> roles = Arrays
                .stream(((MethodSignature) jp.getSignature()).getMethod().getAnnotation(AllowedRoles.class).value())
                .collect(Collectors.toSet());
        UserPrincipal userPrincipal = customUserDetailsService.getUserPrincipal();
        for (RoleData role : roles) {
            if (userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority(role.getDBRoleName()))) {
                return jp.proceed();
            }
        }
        throw new UserPermissionException();
    }
}
