package com.myanime.domain.models.chats;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageModel {
    private Long id;

    private Long conversationId;

    private String senderId;

    private Short messageType;

    private String content;

    private List<MessageAttachmentModel> attachments;

    private LocalDateTime createdAt;

}
