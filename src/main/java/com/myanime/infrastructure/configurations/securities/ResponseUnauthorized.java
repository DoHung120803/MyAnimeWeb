package com.myanime.infrastructure.configurations.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public interface ResponseUnauthorized {
    // trả về lỗi với status và message tùy chỉnh
    default void responseUnauthorized(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", Boolean.FALSE);
        responseBody.put("message", message);

        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        out.print(mapper.writeValueAsString(responseBody));
        out.flush();
        out.close();
    }

    // Phương thức mặc định để trả về lỗi 401 Unauthorized với thông điệp mặc định
    default void responseUnauthorized(HttpServletResponse response) throws IOException {
        responseUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, "Truy cập không hợp lệ");
    }
}
