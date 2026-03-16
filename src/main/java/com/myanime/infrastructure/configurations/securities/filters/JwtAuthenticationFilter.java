package com.myanime.infrastructure.configurations.securities.filters;

import com.myanime.infrastructure.configurations.securities.ResponseUnauthorized;
import com.myanime.infrastructure.configurations.securities.utils.CustomUserDetails;
import com.myanime.infrastructure.configurations.securities.utils.CustomUserDetailService;
import com.myanime.infrastructure.configurations.securities.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter implements ResponseUnauthorized {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                responseUnauthorized(response);
                return;
            }

            String userId = jwtUtil.extractUserId(token);
            CustomUserDetails userDetail = userDetailService.loadUserByUsername(userId);

            if (userDetail == null) {
                responseUnauthorized(response);
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
