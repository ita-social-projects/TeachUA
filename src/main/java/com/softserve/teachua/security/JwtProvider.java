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
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    private static final int TOKEN_LIFE_HOURS = 1;

    //@Value("$(jwt.secret)")
    @Value("${application.jwt.secret}")
    private String jwtSecret;

    /**
     * The method generate jwt token.
     *
     * @return jwt
     */
    public String generateToken(Authentication authentication) {
        log.info("jwtSecret = " + jwtSecret);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, TOKEN_LIFE_HOURS);
        return Jwts.builder().setSubject(userPrincipal.getEmail()).setId(Long.toString(userPrincipal.getId()))
                .setExpiration(calendar.getTime()).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    /**
     * The method retrieve user id from jwt token.
     *
     * @return id
     */
    public Long getUserIdFromToken(String token) {
        //log.info("\t\t\tTOKEN  " + token);
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        String claimsId = claims.getId();
        claimsId = claimsId == null ? "0" : claimsId;
        log.debug("claims.getId() = " + claimsId);
        // return Long.parseLong(claims.getId());
        return Long.parseLong(claimsId);
    }

    /**
     * The method validate token.
     *
     * @return boolean
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // log.error("invalid token"); //Produces many logsgit
        }
        return false;
    }

    /**
     * The method retrieve user email from jwt token.
     *
     * @return email
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }

    /**
     * The method retrieve jwt token from HttpServletRequest.
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
