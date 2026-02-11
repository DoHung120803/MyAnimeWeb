package com.myanime.domain.port.input;

import com.myanime.application.rest.requests.friends.AddFriendRequest;
import com.myanime.application.rest.requests.friends.RespondToFriendRequest;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.models.UserModel;

import java.util.List;

public interface FriendShipUC {
    void addFriend(AddFriendRequest request) throws BadRequestException;

    void respondToFriendRequest(RespondToFriendRequest request) throws BadRequestException;

    List<UserModel> getFriends();

    String getFriendshipStatus(String targetUserId);
}
