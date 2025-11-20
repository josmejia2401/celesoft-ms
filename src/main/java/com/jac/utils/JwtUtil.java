package com.celesoft.utils;

import com.celesoft.utils.dto.SecurityOptionsDTO;
import com.celesoft.utils.exceptions.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Log4j2
@UtilityClass
public class JwtUtil {
    private static final String SECRET = "Jdjf93kL5z8RrjN4Uq3s+w93HkfPxAvMUpEE6nKkt+0=";
    private static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    public static String generateToken(String subject, String tokenId, Long userId, String role, String audience, String appName) {
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

    public static Claims decodeToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(JwtUtil.stripToken(token))
                .getPayload();
    }

    public static boolean isValid(String token) {
        try {
            Claims claims = decodeToken(JwtUtil.stripToken(token));
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static String stripToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return null;
        }
        if (authorizationHeader.toLowerCase().startsWith("bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        return authorizationHeader.trim();
    }

    public static Mono<SecurityOptionsDTO> buildSecurityOptions(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        return Mono.fromCallable(() -> JwtUtil.decodeToken(headers.getFirst(HttpHeaders.AUTHORIZATION)))
                .onErrorResume(e -> Mono.error(new BusinessException("Token inválido o corrupto", HttpStatus.UNAUTHORIZED)))
                .flatMap(claims -> {

                    Object userIdClaim = claims.get("userId");
                    String tokenIdClaim = claims.getId();

                    if (userIdClaim == null || tokenIdClaim == null) {
                        return Mono.error(new BusinessException("Token inválido: faltan claims obligatorios", HttpStatus.UNAUTHORIZED));
                    }

                    Long userId = Long.parseLong(String.valueOf(userIdClaim));
                    Long tokenId = Long.valueOf(tokenIdClaim);

                    String role = (String) claims.get("role");
                    String audience = (String) claims.get("aud");
                    String appName = (String) claims.get("appName");

                    SecurityOptionsDTO options = new SecurityOptionsDTO();
                    options.setUserId(userId);
                    options.setRole(role);
                    options.setTokenId(tokenId);
                    options.setAudience(audience);
                    options.setAppName(appName);
                    return Mono.just(options);
                });
    }
}
