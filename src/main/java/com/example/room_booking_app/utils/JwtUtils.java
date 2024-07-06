package com.example.room_booking_app.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private static final String SECRET_KEY = "WT9ssORnkGcLsQ/mtim6ViWgoVQAVewoth0SAFnJeeE=";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public String extractSubject(String token) {
        return decodeJWT(token).getSubject();
    }
    public Date extractExpiration(String token) {
        return decodeJWT(token).getExpiration();
    }
    public Claims decodeJWT(String jwt) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (SignatureException e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }
    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return encodeJWT(claims, subject);
    }
    private String encodeJWT(Map<String,Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 5000 * 60)) // 5 minutes expiration time
                .signWith(getSigningKey())
                .compact();

    }
    public Boolean validateToken(String token){
        return !extractExpiration(token).before(new Date());
    }
}
