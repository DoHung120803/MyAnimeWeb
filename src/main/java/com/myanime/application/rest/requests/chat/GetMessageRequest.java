package com.myanime.application.rest.requests.chat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMessageRequest {
    @NotNull(message = "Conversation ID không được để trống")
    @Min(value = 1, message = "Conversation ID không hợp lệ")
    private Long conversationId;
}
