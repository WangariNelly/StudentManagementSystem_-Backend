package com.managementsystem.studentsmanagementsystem.services;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtUtil {
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String firstName,String lastName, Collection<? extends GrantedAuthority> authorities) {
        System.out.println("Generating token for username: " + firstName + " " + lastName);

        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(firstName + " " + lastName)
                .claim("firstName", firstName)
                .claim("lastName", lastName)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY,SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractFullName(String token) {
        Claims claims = extractAllClaims(token);
        String firstName = claims.get("firstName", String.class);
        String lastName = claims.get("lastName", String.class);
        return firstName + " " + lastName;
    }

    public boolean isTokenValid(String token, String firstName, String lastName) {
        System.out.println("Validating token for username: " + firstName + " " + lastName);
        String extractedFullName = extractFullName(token);
        String providedFullName = firstName + " " + lastName;
        return extractedFullName.equals(providedFullName) && !isTokenExpired(token);
    }

    public Collection<? extends GrantedAuthority> extractRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        String roles = claims.get("roles", String.class);
        return Arrays.stream(roles.split(","))
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
