package com.myanime.infrastructure.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Interceptor để xác thực JWT token từ STOMP header khi kết nối WebSocket.
 * Token được gửi qua STOMP CONNECT header "Authorization".
 * Sau khi xác thực, Authentication sẽ được đặt vào SecurityContext cho các message tiếp theo.
 */
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Lấy token từ STOMP CONNECT header
            List<String> authHeaders = accessor.getNativeHeader("Authorization");

            if (authHeaders != null && !authHeaders.isEmpty()) {
                String bearerToken = authHeaders.getFirst();

                if (bearerToken.startsWith("Bearer ")) {
                    String token = bearerToken.substring(7);

                    try {
                        Jwt jwt = jwtDecoder.decode(token);
                        Authentication authentication = new JwtAuthenticationToken(jwt, Collections.emptyList());
                        accessor.setUser(authentication);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Invalid JWT token in WebSocket connection", e);
                    }
                }
            }
        }

        return message;
    }
}
