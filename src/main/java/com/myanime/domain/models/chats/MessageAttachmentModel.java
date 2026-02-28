package com.myanime.domain.models.chats;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageAttachmentModel {
    private Long id;
    private Long messageId;
    private Short fileType;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private LocalDateTime createdAt;
}

