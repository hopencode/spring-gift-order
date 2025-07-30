package gift.auth;

import gift.entity.Member;
import gift.exception.MemberExceptions;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtAuth {

    private String jwtKey;

    public String getJwtKey() {
        return jwtKey;
    }
    public void setJwtKey(String jwtKey) {
        this.jwtKey = jwtKey;
    }

    public String createJwtToken(Member member, String accessToken) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(member.getEmail())
                .claim("email", member.getEmail());

        if (accessToken != null) {
            builder.claim("accessToken", accessToken);
        }

        return builder
                .signWith(getSecretKeyFromJWTKey(jwtKey))
                .compact();
    }

    public String createJwtToken(Member member) {
        return createJwtToken(member, null);
    }

    public String getEmailFromToken(String token) {
        SecretKey key = getSecretKeyFromJWTKey(jwtKey);
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("email", String.class);
    }

    public String getAccessTokenFromToken(String token) {
        SecretKey key = getSecretKeyFromJWTKey(jwtKey);
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("accessToken", String.class);
    }

    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKeyFromJWTKey(jwtKey))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new MemberExceptions.InvalidTokenException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            throw new MemberExceptions.InvalidTokenException("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new MemberExceptions.InvalidTokenException("JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
            throw new MemberExceptions.InvalidTokenException("유효하지 않은 JWT 토큰입니다.");
        }
    }

    private SecretKey getSecretKeyFromJWTKey(String jwtKey) {
        return Keys.hmacShaKeyFor(jwtKey.getBytes());
    }
}
