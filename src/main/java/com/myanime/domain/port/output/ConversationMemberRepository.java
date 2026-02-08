package com.myanime.domain.port.output;

import com.myanime.domain.models.chats.ConversationMemberModel;

import java.util.List;

public interface ConversationMemberRepository {
    void saveAll(List<ConversationMemberModel> models);

    List<ConversationMemberModel> findByConversationIds(List<Long> conversationIds);
}
