package com.myanime.infrastructure.configurations.securities.utils;

import com.myanime.domain.dtos.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.signerKey}")
    private String signerKey;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(signerKey.getBytes());
    }

    public JwtDTO generateToken(String userId) {
        return generateToken(userId, null, null);
    }

    public JwtDTO generateToken(String userId, String issuer, java.util.Map<String, Object> extraClaims) {
        Date expireAt = new Date(System.currentTimeMillis() + 3 * 3600 * 1000L); // 3 hours

        var builder = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(expireAt);

        if (issuer != null) builder.setIssuer(issuer);
        if (extraClaims != null) extraClaims.forEach(builder::claim);

        String jwt = builder
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setJwt(jwt);
        jwtDTO.setExpireAt(expireAt.toInstant().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
        jwtDTO.setExpireTime(3 * 3600L);

        return jwtDTO;
    }

    public String extractUserId(String token) {
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

}
