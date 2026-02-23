package com.example.CWMS.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final int jwtExpirationMs = 86400000; // 24h

    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        // On transforme les rôles de la DB en une liste de chaînes pour le JWT
        java.util.List<String> roles = userPrincipal.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.toList());

        return io.jsonwebtoken.Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("authorities", roles) // INDISPENSABLE pour Angular
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date((new java.util.Date()).getTime() + 86400000))
                .signWith(key)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}