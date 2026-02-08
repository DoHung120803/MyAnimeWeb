package com.myanime.domain.port.output;

import com.myanime.domain.models.chats.ConversationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConversationRepository {
    ConversationModel save(ConversationModel conversation);
    ConversationModel findByDirectConversationKey(String directConversationKey);
    boolean existByDirectConversationKey(String directConversationKey);
    Long getIdByDirectKey(String directConversationKey);
    boolean existById(Long conversationId);
    Page<ConversationModel> findConversationsByUserId(String userId, Pageable pageable);
    ConversationModel findById(Long conversationId);
}
