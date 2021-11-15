package com.softserve.teachua.controller.marker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Mark interface that provides "/api" URI for path
 * Implement this interface to add.
 *
 * @author Denis Burko
 */
@RequestMapping("/api")
@CrossOrigin
//@OpenAPIDefinition(info = @Info(title = "Teach UA API", version = "v0.1"))
//@SecurityScheme(
//        name = "api",
//        scheme = "basic",
//        type = SecuritySchemeType.HTTP,
//        in = SecuritySchemeIn.HEADER)
public interface Api {
}
