package com.example.picklaundry_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET =
            "PickLaundrySuperSecretKeyForJwtAuthentication2026PickLaundry";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    // ==========================
    // CUSTOMER TOKEN
    // ==========================

    public String generateToken(Long userId) {

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    // ==========================
    // SUPER ADMIN TOKEN
    // ==========================

    public String generateSuperAdminToken() {

        return Jwts.builder()
                .subject("SUPER_ADMIN")
                .claim("role", "SUPER_ADMIN")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    // ==========================
    // USER ID
    // ==========================

    public String extractUserId(String token) {

        return extractClaims(token).getSubject();
    }

    // ==========================
    // CLAIMS
    // ==========================

    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ==========================
    // EXPIRATION
    // ==========================

    public Date extractExpiration(String token) {

        return extractClaims(token).getExpiration();
    }

    // ==========================
    // VALIDATE
    // ==========================

    public boolean isTokenValid(String token) {

        try {

            Claims claims = extractClaims(token);

            System.out.println("JWT SUBJECT : " + claims.getSubject());
            System.out.println("JWT EXPIRY  : " + claims.getExpiration());

            return claims.getExpiration().after(new Date());

        } catch (JwtException e) {

            System.out.println("JWT INVALID");
            e.printStackTrace();

            return false;
        }
    }
}