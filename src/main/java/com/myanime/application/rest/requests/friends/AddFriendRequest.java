package com.myanime.application.rest.requests.friends;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFriendRequest {
    @NotBlank(message = "Friend User ID không được để trống")
    private String friendUserId;
}
