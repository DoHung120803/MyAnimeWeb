package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMemberJpaRepository extends JpaRepository<ConversationMember, Long> {

    @Query("SELECT cm FROM ConversationMember cm WHERE cm.conversationId IN :conversationIds")
    List<ConversationMember> findByConversationIdIn(List<Long> conversationIds);
}

