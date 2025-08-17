package com.example.CustomerRelationManagementSystem.security.service;

import com.example.CustomerRelationManagementSystem.security.model.UserPrinciple;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;
    private static final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000; // 60 minutes validity
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 days validity

    public String generateAccessToken(UserPrinciple userPrinciple) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userPrinciple.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));

        return buildToken(claims, userPrinciple.getUsername(), ACCESS_TOKEN_VALIDITY);
    }

    public String generateRefreshToken(UserPrinciple userPrinciple) {
        return buildToken(Collections.emptyMap(), userPrinciple.getUsername(), REFRESH_TOKEN_VALIDITY);
    }

    private String buildToken(Map<String, Object> claims, String subject, long validity) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validity))
                .and()
                .signWith(getKey())
                .compact();
    }

    public SecretKey getKey() {
        byte[] bytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(bytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            extractAllClaims(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
