package com.softserve.teachua.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Set;

@Component
public class ReactRouterForwardingFilter extends OncePerRequestFilter {
    private static final Set<String> NOT_CLIENT_PATHS = Set.of(
            "/api/",
            "/oauth2/",
            "/upload/",
            "/swagger-ui.html",
            "/swagger-resources/",
            "/v3/api-docs"
    );

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (isRequestForReactRoute(request)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
            dispatcher.forward(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isRequestForReactRoute(HttpServletRequest request) {
        String path = request.getRequestURI()
                .substring(request.getContextPath().length());

        return isClientPath(path) && !hasFileExtension(path);
    }

    private boolean isClientPath(String path) {
        return NOT_CLIENT_PATHS.stream()
                .noneMatch(path::startsWith);
    }

    private boolean hasFileExtension(String path) {
        return path.lastIndexOf('.') != -1;
    }
}
