package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationMemberJpaRepository extends JpaRepository<ConversationMember, Long> {

    @Query("SELECT cm FROM ConversationMember cm WHERE cm.conversationId IN :conversationIds")
    List<ConversationMember> findByConversationIdIn(List<Long> conversationIds);

    Optional<ConversationMember> findByConversationIdAndUserId(Long conversationId, String userId);

    @Modifying
    @Query("UPDATE ConversationMember cm SET cm.unreadCount = COALESCE(cm.unreadCount, 0) + 1 " +
            "WHERE cm.conversationId = :conversationId AND cm.userId <> :senderUserId")
    void incrementUnreadCountForOtherMembers(Long conversationId, String senderUserId);

    @Modifying
    @Query("UPDATE ConversationMember cm SET cm.unreadCount = 0, cm.lastReadMessageId = :lastReadMessageId " +
            "WHERE cm.conversationId = :conversationId AND cm.userId = :userId")
    void markAsRead(Long conversationId, String userId, Long lastReadMessageId);

    @Query("SELECT COALESCE(SUM(cm.unreadCount), 0) FROM ConversationMember cm WHERE cm.userId = :userId")
    int getTotalUnreadCount(String userId);
}

