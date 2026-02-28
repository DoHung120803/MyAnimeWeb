package com.myanime.domain.service.chat;

import com.myanime.application.rest.requests.chat.SendMessageRequest;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.enums.MessageType;
import com.myanime.domain.models.chats.ConversationModel;
import com.myanime.domain.models.chats.MessageAttachmentModel;
import com.myanime.domain.models.chats.MessageModel;
import com.myanime.domain.port.input.ChatUC;
import com.myanime.domain.port.output.ConversationMemberRepository;
import com.myanime.domain.port.output.ConversationRepository;
import com.myanime.domain.port.output.MessageAttachmentRepository;
import com.myanime.domain.port.output.MessageRepository;
import com.myanime.infrastructure.configurations.securities.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService implements ChatUC {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final MessageRepository messageRepository;
    private final MessageAttachmentRepository messageAttachmentRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageModel sendMessage(SendMessageRequest request, Principal principal) throws BadRequestException {
        MessageType messageType = MessageType.fromType(request.getMessageType());

        validateMessageRequest(request, messageType);

        Long conversationId = request.getConversationId();

        ConversationModel conversation = conversationRepository.findById(conversationId);
        if (conversation == null) {
            throw new BadRequestException("Cuộc trò chuyện không tồn tại");
        }

        // Tạo message
        MessageModel message = new MessageModel();
        message.setConversationId(conversationId);
        message.setMessageType(request.getMessageType());
        message.setContent(request.getContent());
        message.setCreatedAt(LocalDateTime.now());

        String senderId = extractUserId(principal);
        message.setSenderId(senderId);

        // Cập nhật last message
        String lastMessageText = (messageType == MessageType.MEDIA)
                ? "[Media]"
                : request.getContent();

        conversation.setLastMessageTime(LocalDateTime.now());
        conversation.setLastMessageText(lastMessageText);
        conversationRepository.save(conversation);

        // Lưu message
        MessageModel savedMessage = messageRepository.save(message);

        // Lưu attachments nếu là MEDIA
        if (messageType == MessageType.MEDIA) {
            List<MessageAttachmentModel> attachments = buildAttachments(savedMessage.getId(), request.getAttachments());
            List<MessageAttachmentModel> savedAttachments = messageAttachmentRepository.saveAll(attachments);
            savedMessage.setAttachments(savedAttachments);
        }

        // Tăng unreadCount cho tất cả thành viên khác trong conversation
        conversationMemberRepository.incrementUnreadCountForOtherMembers(conversationId, senderId);

        return savedMessage;
    }

    private void validateMessageRequest(SendMessageRequest request, MessageType messageType) throws BadRequestException {
        switch (messageType) {
            case TEXT:
                if (!StringUtils.hasText(request.getContent())) {
                    throw new BadRequestException("Nội dung tin nhắn không được để trống");
                }
                break;
            case MEDIA:
                if (CollectionUtils.isEmpty(request.getAttachments())) {
                    throw new BadRequestException("Tin nhắn media phải có ít nhất một tệp đính kèm");
                }
                break;
            case null:
            default:
                throw new BadRequestException("Loại tin nhắn không hợp lệ");
        }
    }

    private List<MessageAttachmentModel> buildAttachments(Long messageId, List<SendMessageRequest.MediaInfo> mediaInfos) {
        return mediaInfos.stream()
                .map(info -> {
                    MessageAttachmentModel attachment = new MessageAttachmentModel();
                    attachment.setMessageId(messageId);
                    attachment.setFileType(info.getFileType());
                    attachment.setFileUrl(info.getFileUrl());
                    attachment.setFileName(info.getFileName());
                    attachment.setFileSize(info.getFileSize());
                    attachment.setCreatedAt(LocalDateTime.now());
                    return attachment;
                })
                .toList();
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
