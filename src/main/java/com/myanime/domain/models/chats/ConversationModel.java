package com.myanime.domain.models.chats;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConversationModel {
    private Long id;

    private Short type;

    private String name;

    private String chatAvt;

    private LocalDateTime createdAt;

    private String lastMessageText;

    private String directConversationKey;

    private LocalDateTime lastMessageTime;

    private Integer unreadCount;
}
