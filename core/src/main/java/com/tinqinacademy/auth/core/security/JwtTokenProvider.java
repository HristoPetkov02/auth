package com.tinqinacademy.auth.core.security;

import com.tinqinacademy.auth.api.exceptions.AuthApiException;
import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.repository.BlackListedTokenRepository;
import com.tinqinacademy.auth.persistence.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${security.jwt.secret-key}")
    private String JWTSecret;

    @Value("${security.jwt.expiration-time}")
    private Integer JWTExpirationDate;

    private final UserRepository userRepository;
    private final BlackListedTokenRepository blackListedTokenRepository;


    private SecretKey key() {
        byte[] keyBytes = Decoders.BASE64.decode(JWTSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean validateToken(String token) {
        if (blackListedTokenRepository.existsByToken(token)) {
            return false;
        }
        String id;
        String role;
        try {
            id = extractId(token);
            role = extractRole(token);
        }
        catch (AuthApiException ex){
            return false;
        }

        Optional<User> user = userRepository.findById(UUID.fromString(id));

        return user.isPresent() && user.get().getRole().toString().equals(role);
    }

    public String extractId(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    private  <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        try {
            return Jwts
                    .parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch (Exception e){
            throw new AuthApiException("Invalid JWT.", HttpStatus.UNAUTHORIZED);
        }
    }

    public String generateToken(User user){
        return generateToken(new HashMap<>(), user);
    }



    private String generateToken(Map<String,Object> extraClaims, User user) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWTExpirationDate);

        String token = Jwts.builder()
                .claims(extraClaims)
                .subject(user.getId().toString())
                .issuedAt(currentDate)
                .claim("role",user.getRole().name())
                .claim("iat", currentDate)
                .claim("exp", expireDate)
                .expiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }


}
