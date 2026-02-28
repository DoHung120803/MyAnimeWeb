package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.chats.MessageAttachmentModel;
import com.myanime.domain.port.output.MessageAttachmentRepository;
import com.myanime.infrastructure.entities.MessageAttachment;
import com.myanime.infrastructure.jparepos.MessageAttachmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageAttachmentAdapter implements MessageAttachmentRepository {

    private final MessageAttachmentJpaRepository messageAttachmentJpaRepository;

    @Override
    public MessageAttachmentModel save(MessageAttachmentModel model) {
        if (model == null) {
            return null;
        }
        MessageAttachment entity = ModelMapperUtil.mapper(model, MessageAttachment.class);
        MessageAttachment saved = messageAttachmentJpaRepository.save(entity);
        return ModelMapperUtil.mapper(saved, MessageAttachmentModel.class);
    }

    @Override
    public List<MessageAttachmentModel> saveAll(List<MessageAttachmentModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }

        List<MessageAttachment> entities = ModelMapperUtil.mapList(models, MessageAttachment.class);
        List<MessageAttachment> saved = messageAttachmentJpaRepository.saveAll(entities);
        return ModelMapperUtil.mapList(saved, MessageAttachmentModel.class);
    }

    @Override
    public List<MessageAttachmentModel> findByMessageId(Long messageId) {
        if (messageId == null) {
            return Collections.emptyList();
        }
        List<MessageAttachment> entities = messageAttachmentJpaRepository.findByMessageId(messageId);
        return ModelMapperUtil.mapList(entities, MessageAttachmentModel.class);
    }

    @Override
    public List<MessageAttachmentModel> findByMessageIds(List<Long> messageIds) {
        if (CollectionUtils.isEmpty(messageIds)) {
            return Collections.emptyList();
        }
        List<MessageAttachment> entities = messageAttachmentJpaRepository.findByMessageIdIn(messageIds);
        return ModelMapperUtil.mapList(entities, MessageAttachmentModel.class);
    }
}

