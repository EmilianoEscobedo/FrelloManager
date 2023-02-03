package com.laingard.FrelloManager.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class TokenUtils {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static Long jwtExpirationMs = 28800000L;


    public static String createToken(String username, Collection<? extends GrantedAuthority> authorities){
        String authority = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", authority)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(claims.get("roles").toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .toList();
            String username = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return null;
    }
}
