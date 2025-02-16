package cfp.wecare.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(), new HashMap<>(), expiration);
    }

    private String generateToken(String username, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String extractUserName(String jwt) {
        Claims claims = extractClaims(jwt);
        return claims.getSubject();
    }

    private Claims extractClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public boolean isTokenValid(String jwt) {
        Claims claims = extractClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
