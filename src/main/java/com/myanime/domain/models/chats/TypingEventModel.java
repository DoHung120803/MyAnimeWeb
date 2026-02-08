package com.myanime.domain.models.chats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypingEventModel {
    private Long conversationId;
    private String userId;
    private String userName;
    private Boolean isTyping;
}
