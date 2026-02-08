package com.myanime.infrastructure.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Định cấu hình Simple Broker với điểm đến "/conversation"
        registry.enableSimpleBroker("/conversation");

        // Đặt tiền tố cho các điểm đến ứng dụng
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // Đăng ký điểm cuối STOMP tại "/chat"
                .setAllowedOriginPatterns("*") // Cho phép tất cả origins kể cả khi withCredentials=true
                .withSockJS(); // Kích hoạt SockJS như một phương thức dự phòng
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Đăng ký interceptor xác thực JWT cho WebSocket
        registration.interceptors(webSocketAuthInterceptor);
    }
}
