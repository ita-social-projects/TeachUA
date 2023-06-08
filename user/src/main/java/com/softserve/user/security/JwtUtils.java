package com.softserve.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class JwtUtils {
    public static final String USER_ID = "user_id";
    private final String accessTokenSecret;
    private final String refreshTokenSecret;
    private final Integer accessExpirationTimeInMinutes;
    private final Integer refreshExpirationTimeInDays;

    @Autowired
    public JwtUtils(@Value("${application.jwt.access.key}") String accessTokenSecret,
                    @Value("${application.jwt.refresh.key}") String refreshTokenSecret,
                    @Value("${application.jwt.access.expirationInMinutes}") Integer accessExpirationTime,
                    @Value("${application.jwt.refresh.expirationInDays}") Integer refreshExpirationTime) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.accessExpirationTimeInMinutes = accessExpirationTime;
        this.refreshExpirationTimeInDays = refreshExpirationTime;
    }

    /**
     * Method for generating access token.
     */
    public String generateAccessToken(String email) {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.MINUTE, accessExpirationTimeInMinutes);
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(calendar.getTime())
                .signWith(getSignInKey(accessTokenSecret), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Method for generating refresh token.
     */
    public String generateRefreshToken(Long id) {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.DATE, refreshExpirationTimeInDays);
        Claims claims = Jwts.claims();
        claims.put(USER_ID, id);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(calendar.getTime())
                .signWith(getSignInKey(refreshTokenSecret), SignatureAlgorithm.HS512)
                .compact();
    }

    private Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    /**
     * The method validate access token.
     *
     * @return boolean
     */
    public boolean isAccessTokenValid(String accessToken) {
        try {
            return isTokenNotExpired(accessToken, accessTokenSecret);
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
            return isTokenNotExpired(refreshToken, refreshTokenSecret);
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
        return extractClaim(accessToken, accessTokenSecret, Claims::getSubject);
    }

    /**
     * The method retrieve user id from refresh token.
     *
     * @return user id
     */
    public Long getUserIdFromRefreshToken(String refreshToken) {
        return extractClaim(refreshToken, refreshTokenSecret, claims -> claims.get(USER_ID, Long.class));
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

    private boolean isTokenNotExpired(String token, String secret) {
        return !extractExpiration(token, secret).before(new Date());
    }

    public <T> T extractClaim(String token, String secret, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, secret);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token, String secret) {
        return extractClaim(token, secret, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token, String secret) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
