package com.myanime.application.rest.requests.chat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SendMessageRequest {
    @NotNull(message = "Conversation ID không được để trống")
    @Min(value = 1, message = "Conversation ID không hợp lệ")
    private Long conversationId;

    @NotNull(message = "Loại tin nhắn không được để trống")
    private Short messageType;

    // Nullable khi messageType là MEDIA
    private String content;

    @Valid
    private List<MediaInfo> attachments;

    @Getter
    @Setter
    public static class MediaInfo {
        @NotNull(message = "Loại file không được để trống")
        private Short fileType;

        @NotNull(message = "URL không được để trống")
        private String fileUrl;

        private String fileName;

        private Long fileSize;
    }
}
