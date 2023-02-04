package com.softserve.teachua.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class JwtProvider {
    @Value("${application.jwt.accessTokenSecret}")
    private String accessTokenSecret;
    @Value("${application.jwt.refreshTokenSecret}")
    private String refreshTokenSecret;
    @Value("${application.jwt.accessExpirationTimeInMinutes}")
    private int accessExpirationTimeInMinutes;
    @Value("${application.jwt.refreshExpirationTimeInMinutes}")
    private int refreshExpirationTimeInMinutes;

    /**
     * Method for generating access token.
     */
    public String generateAccessToken(String email) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, accessExpirationTimeInMinutes);
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS512, accessTokenSecret)
                .compact();
    }

    /**
     * Method for generating refresh token.
     */
    public String generateRefreshToken(String email) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, refreshExpirationTimeInMinutes);
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS512, refreshTokenSecret)
                .compact();
    }

    /**
     * The method validate access token.
     *
     * @return boolean
     */
    public boolean isAccessTokenValid(String accessToken) {
        try {
            Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * The method validate access token.
     *
     * @return boolean
     */
    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(refreshTokenSecret).parseClaimsJws(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * The method retrieve user email from access token.
     *
     * @return email
     */
    public String getEmailFromAccessToken(String accessToken) {
        Claims claims = Jwts.parser().setSigningKey(accessTokenSecret)
                .parseClaimsJws(accessToken).getBody();
        return claims.getSubject();
    }

    /**
     * The method retrieve user email from refresh token.
     *
     * @return email
     */
    public String getEmailFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parser().setSigningKey(refreshTokenSecret)
                .parseClaimsJws(refreshToken).getBody();
        return claims.getSubject();
    }

    /**
     * The method retrieve jwt token from HttpServletRequest.
     *
     * @return id
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
