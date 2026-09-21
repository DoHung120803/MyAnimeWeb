package com.myanime.infrastructure.configurations.securities.utils;

import com.myanime.domain.dtos.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(signerKey.getBytes());
    }

    public JwtDTO generateToken(String userId) {
        return generateToken(userId, null, expiration);
    }

    public String generateRefreshToken(String userId) {
        return buildToken(userId, null, refreshTokenExpiration);
    }

    public JwtDTO generateToken(String userId, Map<String, Object> extraClaims, long expiration) {
        String token = buildToken(userId, extraClaims, expiration);
        Date expireAt = new Date(System.currentTimeMillis() + expiration * 1000L);

        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setJwt(token);
        jwtDTO.setRefreshToken(generateRefreshToken(userId));
        jwtDTO.setExpireAt(expireAt.toInstant().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
        jwtDTO.setExpireTime(expiration);

        return jwtDTO;
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String buildToken(String userId, Map<String, Object> extraClaims, long expiration) {
        Date expireAt = new Date(System.currentTimeMillis() + expiration * 1000L); // expiration in seconds

        var builder = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(expireAt);

        if (!CollectionUtils.isEmpty(extraClaims)) extraClaims.forEach(builder::claim);

        return builder
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

}
