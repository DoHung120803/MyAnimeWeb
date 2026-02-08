package com.myanime.domain.models.chats;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageModel {
    private Long id;

    private Long conversationId;

    private String senderId;

    private Short messageType;

    private String content;

    private LocalDateTime createdAt;
}
