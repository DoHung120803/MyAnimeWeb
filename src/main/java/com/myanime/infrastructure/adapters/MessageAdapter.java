package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.enums.MessageType;
import com.myanime.domain.models.chats.MessageAttachmentModel;
import com.myanime.domain.models.chats.MessageModel;
import com.myanime.domain.port.output.MessageRepository;
import com.myanime.infrastructure.entities.Message;
import com.myanime.infrastructure.entities.MessageAttachment;
import com.myanime.infrastructure.jparepos.MessageAttachmentJpaRepository;
import com.myanime.infrastructure.jparepos.MessageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageAdapter implements MessageRepository {

    private final MessageJpaRepository messageJpaRepository;
    private final MessageAttachmentJpaRepository messageAttachmentJpaRepository;

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

        Page<MessageModel> models = ModelMapperUtil.mapPage(messages, MessageModel.class);

        if (models.isEmpty()) {
            return models;
        }

        List<Long> messageIds = models.getContent().stream()
                .filter(msg -> MessageType.MEDIA.getType().equals(msg.getMessageType()))
                .map(MessageModel::getId)
                .toList();

        // 3. Fetch attachments
        List<MessageAttachment> attachments = messageAttachmentJpaRepository.findByMessageIdIn(messageIds);

        // 4. Map attachments to models and group by messageId
        Map<Long, List<MessageAttachmentModel>> attachmentsMap = attachments.stream()
                .map(att -> ModelMapperUtil.mapper(att, MessageAttachmentModel.class))
                .collect(Collectors.groupingBy(MessageAttachmentModel::getMessageId));

        // 5. Assign attachments to messages
        models.forEach(model -> model.setAttachments(attachmentsMap.getOrDefault(model.getId(), Collections.emptyList())));

        return models;
    }
}
