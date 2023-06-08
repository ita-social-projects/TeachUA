package com.softserve.teachua.utils.annotation;

import com.softserve.commons.exception.UserPermissionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DevPermitAspect {
    private static final String JWT = "p3s6v9ycRfUj/A?D*G-x/WmZq4t7dRgUjXnq3ThWmYq3t6w9z$C&F?E(H+KbPeShVmYw!z%C*F";
    //private final JwtUtils jwtUtils;
    //private final FileUtils fileUtils;
    //
    //@Autowired
    //public DevPermitAspect(JwtUtils jwtUtils, FileUtils fileUtils) {
    //    this.jwtUtils = jwtUtils;
    //    this.fileUtils = fileUtils;
    //}

    @Around("@annotation(DevPermit)")
    public Object checkJwToken(ProceedingJoinPoint joinPoint) throws Throwable {
        //todo
        //String currentJwt = jwtUtils.getJwtFromRequest(
        //        ((ServletRequestAttributes) Objects.requireNonNull(
        //                RequestContextHolder.getRequestAttributes())).getRequest());
        //if (currentJwt.equals(JWT) && Boolean.TRUE.equals(fileUtils.isKeyFileExists())) {
        //    return joinPoint.proceed();
        //}
        throw new UserPermissionException();
    }
}
