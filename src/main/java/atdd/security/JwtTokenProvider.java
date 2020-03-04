package atdd.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {
    private String secretKey;
    private long expireLength;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey, @Value("${security.jwt.token.expire-length}") long expireLength) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.expireLength = expireLength;
    }

    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUserEmail(String token) {
        String resolvedToken = this.resolveToken(token);

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(resolvedToken)
                .getBody()
                .getSubject();
    }

    private String resolveToken(String token) {

        return Optional.of(token)
                .filter(it -> it.startsWith("Bearer "))
                .map(it -> it.substring(7))
                .orElse("");
    }

    public boolean validateToken(String token) {
        String resolvedToken = this.resolveToken(token);

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(resolvedToken);
            return claims.getBody().getExpiration().after(new Date());
        } catch (JwtException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }
}
