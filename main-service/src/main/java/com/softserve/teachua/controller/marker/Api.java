package com.softserve.teachua.controller.marker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Mark interface that provides "/api" URI for path Implement this interface to add.
 *
 * @author Denis Burko
 */
@CrossOrigin
@OpenAPIDefinition(info = @Info(title = "Teach UA API",
        description = "API for major TeachUA endpoints. For DataTransfer and Logs endpoints, please contact dev team.",
        version = "v0.1"), servers = {
            @Server(description = "localhost", url = "http://localhost:8080/dev"),
            @Server(description = "dev server", url = "https://speak-ukrainian.org.ua/dev/")})
@SecurityScheme(name = "api", scheme = "bearer", bearerFormat = "JWT",
                type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public interface Api {
}
