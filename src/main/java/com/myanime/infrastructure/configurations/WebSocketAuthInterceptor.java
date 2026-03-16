package com.myanime.infrastructure.configurations;

import com.myanime.infrastructure.configurations.securities.utils.CustomUserDetails;
import com.myanime.infrastructure.configurations.securities.utils.CustomUserDetailService;
import com.myanime.infrastructure.configurations.securities.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Interceptor để xác thực JWT token từ STOMP header khi kết nối WebSocket.
 * Token được gửi qua STOMP CONNECT header "Authorization".
 * Sau khi xác thực, Authentication sẽ được đặt vào SecurityContext cho các message tiếp theo.
 */
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailService userDetailService;

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

                    if (!jwtUtil.validateToken(token)) {
                        throw new IllegalArgumentException("Invalid JWT token in WebSocket connection");
                    }

                    String userId = jwtUtil.extractUserId(token);
                    CustomUserDetails userDetail = userDetailService.loadUserByUsername(userId);

                    if (userDetail == null) {
                        throw new IllegalArgumentException("User not found for WebSocket connection");
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                    accessor.setUser(authentication);
                }
            }
        }

        return message;
    }
}
