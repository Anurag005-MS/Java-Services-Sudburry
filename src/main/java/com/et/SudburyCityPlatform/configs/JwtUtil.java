package com.et.SudburyCityPlatform.configs;



import com.et.SudburyCityPlatform.models.jobs.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey =
                Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CustomUserDetails getUserFromToken(String token) {

        Claims claims = extractAllClaims(token);

        Long userId = claims.get("userId", Long.class);
        Long employerId = claims.get("employerId", Long.class);
        String role = claims.get("role", String.class);
        String username = claims.getSubject();

        return new CustomUserDetails(
                userId,
                employerId,
                username,
                role
        );
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

