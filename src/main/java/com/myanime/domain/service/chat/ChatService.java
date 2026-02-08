package com.myanime.domain.service.chat;

import com.myanime.application.rest.requests.chat.SendMessageRequest;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.chats.ConversationModel;
import com.myanime.domain.models.chats.MessageModel;
import com.myanime.domain.port.input.ChatUC;
import com.myanime.domain.port.output.ConversationRepository;
import com.myanime.domain.port.output.MessageRepository;
import com.myanime.infrastructure.configurations.securities.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService implements ChatUC {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageModel sendMessage(SendMessageRequest request, Principal principal) throws BadRequestException {
        Long conversationId = request.getConversationId();

        ConversationModel conversation = conversationRepository.findById(conversationId);

        if (conversation == null) {
            throw new BadRequestException("Cuộc trò chuyện không tồn tại");
        }

        MessageModel message = ModelMapperUtil.mapper(request, MessageModel.class);
        message.setCreatedAt(LocalDateTime.now());

        // Lấy userId từ Principal (STOMP WebSocket) hoặc fallback SecurityContext (HTTP)
        String senderId = extractUserId(principal);
        message.setSenderId(senderId);

        conversation.setLastMessageTime(LocalDateTime.now());
        conversation.setLastMessageText(message.getContent());
        conversationRepository.save(conversation);

        return messageRepository.save(message);
    }

    /**
     * Lấy userId từ Principal (cho WebSocket STOMP) hoặc fallback về SecurityContext (cho HTTP)
     */
    private String extractUserId(Principal principal) {
        if (principal instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return jwt.getSubject();
        }
        // Fallback cho HTTP request
        return jwtUtil.getCurrentUserId();
    }
}
