package com.myanime.application.rest.requests.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDirectConversationRequest {
    @NotBlank(message = "First User ID không được để trống")
    private String firstUserId;

    @NotBlank(message = "Second User ID không được để trống")
    private String secondUserId;
}
