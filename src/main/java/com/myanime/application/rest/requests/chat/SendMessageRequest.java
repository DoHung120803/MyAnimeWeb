package com.myanime.application.rest.requests.chat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {
    @NotNull(message = "Conversation ID không được để trống")
    @Min(value = 1, message = "Conversation ID không hợp lệ")
    private Long conversationId;

    private Short messageType;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    private String content;
}
