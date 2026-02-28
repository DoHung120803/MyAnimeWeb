package com.myanime.application.rest.requests.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateConservationRequest {
    private String name;

    private String lastMessageText;

    @NotBlank(message = "usersId không được để trống")
    private String userId;
}
