package com.myanime.application.rest.controllers;

import com.myanime.application.rest.requests.friends.AddFriendRequest;
import com.myanime.application.rest.requests.friends.RespondToFriendRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.input.FriendShipUC;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friendships")
public class FriendShipController {

    private final FriendShipUC friendShipUC;

    @PostMapping("/add")
    public ApiResponse<Void> addFriend(@RequestBody @Valid AddFriendRequest request) throws BadRequestException {
        friendShipUC.addFriend(request);
        return ApiResponse.<Void>builder()
                .message("Yêu cầu kết bạn đã được gửi")
                .build();
    }

    @PostMapping("/respond")
    public ApiResponse<Void> respondToFriendRequest(@RequestBody @Valid RespondToFriendRequest request) throws BadRequestException {
        friendShipUC.respondToFriendRequest(request);
        return ApiResponse.<Void>builder()
                .message("Phản hồi lời mời kết bạn đã được gửi")
                .build();
    }

    @PostMapping("/get-friends")
    public ApiResponse<List<UserModel>> getFriends() {
        List<UserModel> data = friendShipUC.getFriends();
        return ApiResponse.<List<UserModel>>builder()
                .data(data)
                .message("Lấy danh sách bạn bè thành công")
                .build();
    }

    @GetMapping("/status/{targetUserId}")
    public ApiResponse<String> getFriendshipStatus(@PathVariable String targetUserId) {
        String status = friendShipUC.getFriendshipStatus(targetUserId);
        return ApiResponse.<String>builder()
                .data(status)
                .message("Lấy trạng thái kết bạn thành công")
                .build();
    }
}
