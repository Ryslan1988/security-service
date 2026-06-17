package by.javaguru.jdmik12.securityservice.service;

import by.javaguru.jdmik12.securityservice.config.TokenProperties;
import by.javaguru.jdmik12.securityservice.model.TokenPair;
import by.javaguru.jdmik12.securityservice.utils.SecurityValidationResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProperties tokenProperties;

    private static final String ACCESS_PREFIX = "access:";
    private static final String REFRESH_PREFIX = "refresh:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public TokenPair generateTokenPair(long userId, String role) {
        String accessToken = buildToken(userId, role, tokenProperties.accessTokenExpiration());
        String refreshToken = buildToken(userId, role, tokenProperties.refreshTokenExpiration());

        redisTemplate.opsForValue().set(
                ACCESS_PREFIX + userId,
                accessToken,
                Duration.ofSeconds(tokenProperties.accessTokenExpiration()));

        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + userId,
                refreshToken,
                Duration.ofSeconds(tokenProperties.refreshTokenExpiration()));

        return new TokenPair(accessToken, refreshToken);
    }

    private String buildToken(long userId, String role, long expirationSeconds) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(getSigningKey())
                .compact();
    }


    public SecurityValidationResult validate(String token) {
        if (isBlacklisted(token)) {
            return SecurityValidationResult.rejected("Token is revoked");
        }

        Claims claims;
        try {
            claims = parseClaims(token);
        } catch (ExpiredJwtException e) {
            return SecurityValidationResult.rejected("Token expired");
        } catch (JwtException e) {
            return SecurityValidationResult.rejected("Invalid token");
        }

        long userId = Long.parseLong(claims.getSubject());
        String role = claims.get("role", String.class);

        String storedToken = redisTemplate.opsForValue().get(ACCESS_PREFIX + userId);
        if (storedToken == null || !storedToken.equals(token)) {
            return SecurityValidationResult.rejected("Token not found or replaced");
        }

        return SecurityValidationResult.passed(userId, role);
    }

    public TokenPair refresh(String refreshToken) {
        Claims claims;
        try {
            claims = parseClaims(refreshToken);
        } catch (JwtException e) {
            throw new SecurityException("Invalid refresh token");
        }

        long userId = Long.parseLong(claims.getSubject());
        String role = claims.get("role", String.class);

        String stored = redisTemplate.opsForValue().get(REFRESH_PREFIX + userId);
        if (stored == null || !stored.equals(refreshToken)) {
            throw new SecurityException("Refresh token mismatch or expired");
        }

        String oldAccess = redisTemplate.opsForValue().get(ACCESS_PREFIX + userId);
        if (oldAccess != null) {
            revoke(oldAccess);
        }

        return generateTokenPair(userId, role);
    }

    public void revoke(String token) {
        Claims claims;
        try {
            claims = parseClaims(token);
        } catch (JwtException e) {
            return;
        }

        long userId = Long.parseLong(claims.getSubject());
        Date expiration = claims.getExpiration();
        long ttl = expiration.getTime() - System.currentTimeMillis();

        if (ttl > 0) {
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + token,
                    "revoked",
                    Duration.ofMillis(ttl));
        }

        redisTemplate.delete(ACCESS_PREFIX + userId);
        redisTemplate.delete(REFRESH_PREFIX + userId);
    }

    private boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenProperties.secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
