package com.myanime.domain.port.output;

import com.myanime.domain.models.chats.MessageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepository {
    MessageModel save(MessageModel messageModel);
    Page<MessageModel> getConversationMessages(Long conversationId, Pageable pageable);
}
