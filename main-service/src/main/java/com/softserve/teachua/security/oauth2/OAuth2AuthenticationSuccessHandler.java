package com.softserve.teachua.security.oauth2;

import com.softserve.clients.exception.BadRequestException;
import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.model.User;
import com.softserve.teachua.security.JwtUtils;
import static com.softserve.teachua.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.softserve.teachua.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.USER_ROLE_PARAMETER;
import com.softserve.teachua.security.util.CookieUtils;
import com.softserve.teachua.service.RefreshTokenService;
import com.softserve.teachua.service.RoleService;
import com.softserve.teachua.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@Transactional
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;

    @Value("#{'${authorizedRedirectUris}'.split(',')}")
    private List<String> authorizedRedirectUris;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    OAuth2AuthenticationSuccessHandler(JwtUtils jwtUtils,
                                       HttpCookieOAuth2AuthorizationRequestRepository
                                               httpCookieOAuth2AuthorizationRequestRepository, UserService userService,
                                       RoleService roleService, RefreshTokenService refreshTokenService) {
        this.jwtUtils = jwtUtils;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * The method handle OAuth2 authentication process.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * The retrieve cookie from request, add redirect uri.
     *
     * @return uri
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        Optional<Object> userRole = CookieUtils.getCookie(request, USER_ROLE_PARAMETER).map(Cookie::getValue);
        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException(
                    "Sorry! We've got an Unauthorized Redirect URI and can't proceed " + "with the authentication");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        User user = userService.getUserByEmail(authentication.getName());
        if (userRole.isPresent() && userRole.get() != "") {
            user.setRole(roleService.findByName((String) userRole.get()));
            log.debug("Set user role" + userRole.get());
        } else {
            user.setRole(roleService.findByName(RoleData.USER.getDBRoleName()));
            log.debug("Set default user role");
        }
        userService.updateUser(user);

        String accessToken = jwtUtils.generateAccessToken(user.getEmail());
        String refreshToken = refreshTokenService.assignRefreshToken(user);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("id", user.getId())
                .queryParam("role", user.getRole().getName())
                .build().toUriString();
    }

    /**
     * The method clear cookie from HttpServletRequest.
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    /**
     * The method check if redirect uri from request equals from.
     *
     * @return OAuth2AuthorizationRequest
     */
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        return authorizedRedirectUris.stream().anyMatch(authorizedRedirectUri -> {
            URI authorizedURI = URI.create(authorizedRedirectUri);
            return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                    && authorizedURI.getPort() == clientRedirectUri.getPort();
        });
    }
}
