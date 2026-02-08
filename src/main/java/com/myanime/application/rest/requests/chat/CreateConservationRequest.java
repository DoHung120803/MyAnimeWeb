package com.myanime.application.rest.requests.chat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateConservationRequest {
    private String name;

    @NotNull(message = "Type không được để trống")
    private Short type;
    private String lastMessageText;

    @NotNull(message = "usersIds không được để trống")
    @NotEmpty(message = "usersIds không được để trống")
    private List<String> userIds;
}
