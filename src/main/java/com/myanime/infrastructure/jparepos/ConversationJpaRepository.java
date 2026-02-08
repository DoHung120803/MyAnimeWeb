package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationJpaRepository extends JpaRepository<Conversation, Long> {
    Conversation findByDirectConversationKey(String directConversationKey);

    boolean existsByDirectConversationKey(String directConversationKey);

    @Query("SELECT c.id FROM Conversation c WHERE c.directConversationKey = ?1")
    Long getIdByDirectConversationKey(String directConversationKey);

    @Query("""
            SELECT DISTINCT c FROM Conversation c
            JOIN ConversationMember cm ON c.id = cm.conversationId
            WHERE cm.userId = :userId
            ORDER BY c.lastMessageTime DESC
            """)
    Page<Conversation> findConversationsByUserId(String userId, Pageable pageable);
}
