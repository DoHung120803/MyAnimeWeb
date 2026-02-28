package com.myanime.domain.service.chat;

import com.myanime.application.rest.requests.chat.CreateConservationRequest;
import com.myanime.application.rest.requests.chat.GetDirectConversationRequest;
import com.myanime.application.rest.requests.chat.GetMessageRequest;
import com.myanime.application.rest.requests.chat.MarkAsReadRequest;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.common.utils.AuthUtil;
import com.myanime.domain.enums.ConversationType;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.models.chats.ConversationMemberModel;
import com.myanime.domain.models.chats.ConversationModel;
import com.myanime.domain.models.chats.MessageModel;
import com.myanime.domain.port.input.ConversationUC;
import com.myanime.domain.port.output.ConversationMemberRepository;
import com.myanime.domain.port.output.ConversationRepository;
import com.myanime.domain.port.output.MessageRepository;
import com.myanime.domain.port.output.UserRepository;
import com.myanime.infrastructure.configurations.securities.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService implements ConversationUC {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final MessageRepository messageRepository;

    private final JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDirectConservation(CreateConservationRequest request) throws BadRequestException {
        String currentUserId = AuthUtil.getCurrentUserId();
        String lastMessageText = request.getLastMessageText();

        List<String> userIds = List.of(currentUserId, request.getUserId());

        long existingUserCount = userRepository.countByIdIn(userIds);
        if (existingUserCount != userIds.size()) {
            throw new BadRequestException("Người dùng không tồn tại");
        }

        ConversationModel conversation = new ConversationModel();

        String directConversationKey = generateHash(currentUserId, request.getUserId());

        if (conversationRepository.existByDirectConversationKey(directConversationKey)) {
            throw new BadRequestException("Cuộc trò chuyện trực tiếp giữa hai người dùng đã tồn tại");
        } else conversation.setDirectConversationKey(directConversationKey);

        conversation.setName(request.getName());

        conversation.setType(ConversationType.DIRECT.getType());
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setLastMessageText(lastMessageText);
        conversation.setLastMessageTime(LocalDateTime.now());

        Long conversationId = conversationRepository.save(conversation).getId();

        List<ConversationMemberModel> members = userIds.stream().map(userId -> {
            ConversationMemberModel member = new ConversationMemberModel();
            member.setConversationId(conversationId);
            member.setUserId(userId);
            member.setJoinedAt(LocalDateTime.now());
            return member;
        }).toList();

        conversationMemberRepository.saveAll(members);

        // Lưu tin nhắn đầu tiên vào bảng message
        if (!StringUtils.hasText(lastMessageText)) return;

        MessageModel firstMessage = new MessageModel();
        firstMessage.setConversationId(conversationId);
        firstMessage.setSenderId(currentUserId);
        firstMessage.setContent(lastMessageText);
        firstMessage.setMessageType((short) 1); // 1 = TEXT (khớp với MessageType.TEXT)
        firstMessage.setCreatedAt(LocalDateTime.now());
        messageRepository.save(firstMessage);

    }

    @Override
    public Object getMessages(GetMessageRequest request, Pageable pageable) {
        Long conversationId = request.getConversationId();

        Page<MessageModel> messages = messageRepository.getConversationMessages(conversationId, pageable);

        return PageResponse.<MessageModel>builder()
                .content(messages.getContent())
                .currentPage(messages.getNumber() + 1)
                .pageSize(messages.getSize())
                .totalElements(messages.getTotalElements())
                .totalPages(messages.getTotalPages())
                .build();
    }

    @Override
    public Object getDirectConversationMessages(GetDirectConversationRequest request, Pageable pageable) {
        String firstUserId = AuthUtil.getCurrentUserId();
        String secondUserId = request.getSecondUserId();

        Long conversationId = conversationRepository.getIdByDirectKey(generateHash(firstUserId, secondUserId));

        PageResponse<MessageModel> messages = new PageResponse<>();

        if (conversationId == null) {
            return messages;
        }

        Page<MessageModel> messagePage = messageRepository.getConversationMessages(conversationId, pageable);

        return PageResponse.<MessageModel>builder()
                .content(messagePage.getContent())
                .currentPage(messagePage.getNumber() + 1)
                .pageSize(messagePage.getSize())
                .totalElements(messagePage.getTotalElements())
                .totalPages(messagePage.getTotalPages())
                .build();
    }

    private String generateHash(String firstUserId, String secondUserId) {
        String[] ids = {firstUserId, secondUserId};
        Arrays.sort(ids);

        String rawKey = ids[0] + "_" + ids[1];

        // Kết quả trả về chuỗi Hex 32 ký tự
        return DigestUtils.md5DigestAsHex(rawKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public PageResponse<ConversationModel> getUserConversations(Pageable pageable) {
        String currentUserId = jwtUtil.getCurrentUserId();
        Page<ConversationModel> conversations = conversationRepository.findConversationsByUserId(currentUserId, pageable);

        List<ConversationModel> content = conversations.getContent();

        if (content.isEmpty()) {
            return PageResponse.<ConversationModel>builder()
                    .content(content)
                    .currentPage(conversations.getNumber() + 1)
                    .pageSize(conversations.getSize())
                    .totalElements(conversations.getTotalElements())
                    .totalPages(conversations.getTotalPages())
                    .build();
        }

        // Lấy tất cả conversation IDs
        List<Long> allConversationIds = content.stream()
                .map(ConversationModel::getId)
                .toList();

        // Lấy tất cả conversation IDs của các cuộc trò chuyện trực tiếp, group thì tạm thời chưa xử lý avatar
        List<Long> directConversationIds = content.stream()
                .filter(c -> ConversationType.DIRECT.getType().equals(c.getType()))
                .map(ConversationModel::getId)
                .toList();

        // Get all member IDs for ALL conversations in ONE database call
        List<ConversationMemberModel> conversationMembers = conversationMemberRepository.findByConversationIds(allConversationIds);

        // Tạo Map unreadCount theo conversationId cho current user
        Map<Long, Integer> unreadCountMap = conversationMembers.stream()
                .filter(member -> member.getUserId().equals(currentUserId))
                .collect(Collectors.toMap(
                        ConversationMemberModel::getConversationId,
                        member -> member.getUnreadCount() != null ? member.getUnreadCount() : 0
                ));

        // tạo Map để lưu other user IDs cần lấy avatar trong tất cả các cuộc trò chuyện
        Map<Long, String> otherUserIdsMap = conversationMembers.stream()
                .filter(member -> !member.getUserId().equals(currentUserId)
                        && directConversationIds.contains(member.getConversationId()))
                .collect(Collectors.toMap(
                        ConversationMemberModel::getConversationId,
                        ConversationMemberModel::getUserId
                ));

        // Lấy tất cả user avatars cuả các other user
        Map<String, UserModel> userModelMap = userRepository.getConversationUserInfo(otherUserIdsMap.values().stream().toList())
                .stream()
                .collect(Collectors.toMap(
                        UserModel::getId,
                        Function.identity()
                ));


        // Populate chat avatar và unreadCount for each conversation
        List<ConversationModel> conversationModels = content.stream()
                .peek(conversation -> {
                    // Set unreadCount cho từng conversation
                    conversation.setUnreadCount(unreadCountMap.getOrDefault(conversation.getId(), 0));

                    if (ConversationType.DIRECT.getType().equals(conversation.getType())) {
                        String otherUserId = otherUserIdsMap.get(conversation.getId());
                        UserModel userModel = userModelMap.get(otherUserId);

                        conversation.setChatAvt(userModel.getAvtUrl());
                        conversation.setName(userModel.getFirstName() + " " + userModel.getLastName());
                    }
                })
                .toList();

        return PageResponse.<ConversationModel>builder()
                .content(conversationModels)
                .currentPage(conversations.getNumber() + 1)
                .pageSize(conversations.getSize())
                .totalElements(conversations.getTotalElements())
                .totalPages(conversations.getTotalPages())
                .build();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(MarkAsReadRequest request) {
        String currentUserId = jwtUtil.getCurrentUserId();
        conversationMemberRepository.markAsRead(
                request.getConversationId(),
                currentUserId,
                request.getLastReadMessageId()
        );
    }

    @Override
    public int getTotalUnreadCount() {
        String currentUserId = jwtUtil.getCurrentUserId();
        return conversationMemberRepository.getTotalUnreadCount(currentUserId);
    }
}
