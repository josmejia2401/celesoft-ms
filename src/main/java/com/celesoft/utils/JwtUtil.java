package com.celesoft.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "Jdjf93kL5z8RrjN4Uq3s+w93HkfPxAvMUpEE6nKkt+0=";
    private static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    public String generateToken(String subject, String tokenId, Long userId, String role, String audience, String appName) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(subject)
                .id(tokenId)
                .audience().add(audience).and()
                .issuer(appName)
                .claim("role", role)
                .claim("userId", userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600))) // 1h
                .signWith(key)
                .compact();
    }

    public Claims decodeToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token) {
        try {
            Claims claims = decodeToken(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
