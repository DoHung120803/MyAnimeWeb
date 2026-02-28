package com.myanime.domain.models.chats;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConversationMemberModel {
    private Long id;

    private Long conversationId;

    private String userId;

    private Long lastReadMessageId;

    private Integer unreadCount;

    private LocalDateTime joinedAt;
}
