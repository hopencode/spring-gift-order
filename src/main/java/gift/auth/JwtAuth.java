package gift.auth;

import gift.entity.Member;
import gift.exception.MemberExceptions;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@PropertySource("classpath:application.properties")
public class JwtAuth {

    @Value("${jwt.key}")
    private String jwtKey;

    public String createJwtToken(Member member){
        String accessToken = Jwts.builder()
                .setSubject(member.getEmail())
                .claim("email", member.getEmail())
                .signWith(getSecretKeyFromJWTKey(jwtKey))
                .compact();
        return accessToken;
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

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKeyFromJWTKey(jwtKey))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
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
