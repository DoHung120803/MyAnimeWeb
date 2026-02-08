package com.myanime.infrastructure.configurations.securities.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    
    private final JwtDecoder jwtDecoder;

    public JwtUtil(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * Extracts JWT token from Authorization header
     * @param request HTTP request
     * @return JWT token string (without "Bearer " prefix), or null if not found
     */
    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Extracts userId from JWT token
     * @param token JWT token string (without "Bearer " prefix)
     * @return userId from the "sub" claim
     */
    public String getUserIdFromToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    /**
     * Extracts userId from current authenticated user (from SecurityContext)
     * @return userId from the "sub" claim of the authenticated JWT token
     */
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return jwt.getSubject();
        }
        return null;
    }

    /**
     * Extracts userId from HTTP request
     * @param request HTTP request
     * @return userId from the "sub" claim
     */
    public String getUserId(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            return getUserIdFromToken(token);
        }
        return null;
    }
}
