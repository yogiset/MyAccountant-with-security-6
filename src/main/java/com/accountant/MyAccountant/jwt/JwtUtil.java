package com.accountant.MyAccountant.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private final SecretKey secretKey;
    private static final long jwtExpirationInMillis = 1000 * 60 * 60 * 10; // 10 hours

    public JwtUtil() {
        // Generate a secure key for HS256 algorithm in the constructor
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
    public String extractuserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username, String role,String name) {
        Map<String, Object> Claims = new HashMap<>();
        Claims.put("role", role);
        Claims.put("name", name);
        return createToken(Claims,username);
    }
    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractuserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
    public String extractRole(String token) {
        return extractClaim(token, claims -> (String) claims.get("role"));
    }

    public Claims validateAndExtractClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            // Check token expiration
            if (claims.getExpiration().before(new Date())) {
                throw new ExpiredJwtException(null, claims, "Token has expired");
            }

            return claims;
        } catch (ExpiredJwtException ex) {
            // Handle token expiration
            throw ex;
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            // Handle invalid tokens
            throw new RuntimeException("Invalid token");
        }
    }
}
