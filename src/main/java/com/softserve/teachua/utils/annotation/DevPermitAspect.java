package com.softserve.teachua.utils.annotation;

import com.softserve.teachua.exception.UserPermissionException;
import com.softserve.teachua.security.JwtUtils;
import com.softserve.teachua.tools.FileUtils;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class DevPermitAspect {
    private static final String JWT = "p3s6v9ycRfUj/A?D*G-x/WmZq4t7dRgUjXnq3ThWmYq3t6w9z$C&F?E(H+KbPeShVmYw!z%C*F";
    private final JwtUtils jwtUtils;
    private final FileUtils fileUtils;

    @Autowired
    public DevPermitAspect(JwtUtils jwtUtils, FileUtils fileUtils) {
        this.jwtUtils = jwtUtils;
        this.fileUtils = fileUtils;
    }

    @Around("@annotation(DevPermit)")
    public Object checkJwToken(ProceedingJoinPoint joinPoint) throws Throwable {
        String currentJwt = jwtUtils.getJwtFromRequest(
                ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getRequest());
        if (currentJwt.equals(JWT) && Boolean.TRUE.equals(fileUtils.isKeyFileExists())) {
            return joinPoint.proceed();
        }
        throw new UserPermissionException();
    }
}
