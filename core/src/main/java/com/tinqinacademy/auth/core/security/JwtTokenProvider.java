package com.tinqinacademy.auth.core.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.secret-key}")
    private String JWTSecret;

    @Value("${security.jwt.expiration-time}")
    private Integer JWTExpirationDate;


    private SecretKey key() {
        byte[] keyBytes = Decoders.BASE64.decode(JWTSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public String generateToken(Authentication authentication) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWTExpirationDate);

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String token = Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(currentDate)
                .claim("roles", roles)
                .claim("iat", currentDate.getTime()/ 1000)
                .claim("exp", expireDate.getTime()/ 1000)
                .expiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }


}
