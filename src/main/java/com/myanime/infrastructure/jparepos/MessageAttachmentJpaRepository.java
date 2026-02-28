package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageAttachmentJpaRepository extends JpaRepository<MessageAttachment, Long> {
    List<MessageAttachment> findByMessageId(Long messageId);
    List<MessageAttachment> findByMessageIdIn(List<Long> messageIds);
}

