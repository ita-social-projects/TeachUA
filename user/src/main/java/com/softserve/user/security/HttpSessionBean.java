package com.softserve.user.security;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Data
@Component
@SessionScope
public class HttpSessionBean {
    private String jwt;
}
