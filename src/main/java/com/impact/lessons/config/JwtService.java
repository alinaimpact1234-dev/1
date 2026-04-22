package com.impact.lessons.config;

import com.impact.lessons.entity.UserPersonalData;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "very_secret_key_very_secret_key_12345";

    public String generateAccessToken(String username,
                                      String role,
                                      UserPersonalData personalData) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", personalData.getUserId())
                .claim("role", role)
                .claim("firstName", personalData.getFirstName())
                .claim("lastName", personalData.getFirstName())
                .claim("BirthDate", personalData.getBirthDate().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }


    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}