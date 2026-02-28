package com.myanime.application.rest.requests.friends;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RespondToFriendRequest {
    @NotBlank(message = "ID không được để trống")
    private String id;

    @NotNull(message = "Phải chấp nhận hoặc từ chối lời mời kết bạn")
    private Boolean isAccept;
}
