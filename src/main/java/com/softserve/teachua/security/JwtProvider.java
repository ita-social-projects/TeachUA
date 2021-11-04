package com.softserve.teachua.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    private final int TOKEN_LIFE_DAYS = 1;

    @Value("$(jwt.secret)")
    private String jwtSecret;

    /**
     * The method generate jwt token
     *
     * @return jwt
     */
    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date date = Date.from(LocalDate.now()
                .plusDays(TOKEN_LIFE_DAYS)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setId(Long.toString(userPrincipal.getId()))
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * The method retrieve user id from jwt token
     *
     * @return id
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        String claimsId = claims.getId();
        claimsId = claimsId == null ? "0" : claimsId;
        return Long.parseLong(claimsId);
    }

    /**
     * The method validate token
     *
     * @return boolean
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.debug("Invalid token", e);
        }
        return false;
    }

    /**
     * The method retrieve user email from jwt token
     *
     * @return email
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    /**
     * The method retrieve jwt token from HttpServletRequest
     *
     * @return id
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }


}
