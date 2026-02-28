package com.myanime.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationModel {
    private Long id;
    private String userId;
    private String senderId;
    private String senderUsername;
    private String type;
    private String referenceId;
    private String content;
    private String payload;
    private Boolean isRead;
    private LocalDateTime createdAt;
}

