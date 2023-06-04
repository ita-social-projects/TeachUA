package com.softserve.certificate.utils.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AllowedRolesAspect {
    @Around("@annotation(com.softserve.certificate.utils.annotation.AllowedRoles)")
    public Object doSomething(ProceedingJoinPoint jp) throws Throwable {
        //Set<RoleData> roles = Arrays
        //        .stream(((MethodSignature) jp.getSignature()).getMethod().getAnnotation(AllowedRoles.class).value())
        //        .collect(Collectors.toSet());
        //UserPrincipal userPrincipal = customUserDetailsService.getUserPrincipal();
        //for (RoleData role : roles) {
        //    if (userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority(role.getDBRoleName()))) {
        //        return jp.proceed();
        //    }
        //}
        //throw new UserPermissionException();
        //todo
        return null;
    }
}
