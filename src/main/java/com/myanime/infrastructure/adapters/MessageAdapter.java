package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.chats.MessageModel;
import com.myanime.domain.port.output.MessageRepository;
import com.myanime.infrastructure.entities.Message;
import com.myanime.infrastructure.jparepos.MessageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageAdapter implements MessageRepository {

    private final MessageJpaRepository messageJpaRepository;

    @Override
    public MessageModel save(MessageModel messageModel) {
        if (messageModel == null) {
            return null;
        }

        Message entity = ModelMapperUtil.mapper(messageModel, Message.class);
        Message saved = messageJpaRepository.save(entity);
        return ModelMapperUtil.mapper(saved, MessageModel.class);
    }

    @Override
    public Page<MessageModel> getConversationMessages(Long conversationId, Pageable pageable) {
        if (conversationId == null) {
            return Page.empty();
        }

        Page<Message> messages = messageJpaRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);

        return ModelMapperUtil.mapPage(messages, MessageModel.class);
    }
}
