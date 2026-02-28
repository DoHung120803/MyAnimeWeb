package com.myanime.domain.port.output;

import com.myanime.domain.models.chats.ConversationMemberModel;

import java.util.List;

public interface ConversationMemberRepository {
    void saveAll(List<ConversationMemberModel> models);

    List<ConversationMemberModel> findByConversationIds(List<Long> conversationIds);

    void incrementUnreadCountForOtherMembers(Long conversationId, String senderUserId);

    void markAsRead(Long conversationId, String userId, Long lastReadMessageId);

    ConversationMemberModel findByConversationIdAndUserId(Long conversationId, String userId);

    int getTotalUnreadCount(String userId);
}
