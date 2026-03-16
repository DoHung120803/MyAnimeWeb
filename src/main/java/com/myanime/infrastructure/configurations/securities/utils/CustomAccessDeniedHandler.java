package com.myanime.infrastructure.configurations.securities.utils;

import com.myanime.infrastructure.configurations.securities.ResponseUnauthorized;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler, ResponseUnauthorized {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        responseUnauthorized(response, HttpServletResponse.SC_FORBIDDEN, "Không có quyền truy cập");
    }
}
