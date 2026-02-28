package com.myanime.domain.port.output;

import com.myanime.domain.models.chats.MessageAttachmentModel;

import java.util.List;

public interface MessageAttachmentRepository {
    MessageAttachmentModel save(MessageAttachmentModel model);
    List<MessageAttachmentModel> saveAll(List<MessageAttachmentModel> models);
    List<MessageAttachmentModel> findByMessageId(Long messageId);
    List<MessageAttachmentModel> findByMessageIds(List<Long> messageIds);
}

