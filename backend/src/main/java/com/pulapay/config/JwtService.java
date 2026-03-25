package com.pulapay.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey())
                .compact();
    }

    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload());
    }
    public boolean isValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername()) && extractClaim(token, Claims::getExpiration).after(new Date());
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
