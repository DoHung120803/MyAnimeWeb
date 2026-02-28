package com.myanime.domain.service.friends;

import com.myanime.application.rest.requests.friends.AddFriendRequest;
import com.myanime.application.rest.requests.friends.RespondToFriendRequest;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.common.utils.AuthUtil;
import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.dtos.friends.FriendshipStatusDTO;
import com.myanime.domain.enums.FriendShipStatus;
import com.myanime.domain.enums.NotificationEventType;
import com.myanime.domain.models.FriendShipModel;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.input.FriendShipUC;
import com.myanime.domain.port.output.FriendShipRepository;
import com.myanime.domain.port.output.UserRepository;
import com.myanime.domain.service.notifies.PostNotificationService;
import com.myanime.domain.service.notifies.builder.NotificationEventFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FriendShipService implements FriendShipUC {

    private final UserRepository userRepository;
    private final FriendShipRepository friendShipRepository;
    private final PostNotificationService postNotificationService;
    private final NotificationEventFactory notificationEventFactory;

    @Override
    public void addFriend(AddFriendRequest request) throws BadRequestException {
        String userId = AuthUtil.getCurrentUserId();
        String friendUserId = request.getFriendUserId();

        if (!userRepository.existsById(friendUserId))
            throw new BadRequestException("Người dùng bạn muốn kết bạn không tồn tại");

        String lowerId = userId.compareTo(friendUserId) < 0 ? userId : friendUserId;
        String higherId = userId.compareTo(friendUserId) < 0 ? friendUserId : userId;

        FriendShipModel friendShipModel = new FriendShipModel();
        friendShipModel.setLowUserId(lowerId);
        friendShipModel.setHighUserId(higherId);
        friendShipModel.setStatus(FriendShipStatus.SENT.getCode());
        friendShipModel.setRequesterUserId(userId);

        if (friendShipRepository.existsByUserIds(lowerId, higherId))
            throw new BadRequestException("Bạn đã gửi lời mời kết bạn hoặc đã là bạn bè với người này");

        FriendShipModel saved = friendShipRepository.save(friendShipModel);

        // Gửi thông báo realtime tới người nhận lời mời kết bạn
        postNotificationService.post(notificationEventFactory.build(
                NotificationEventType.FRIEND_REQUEST,
                friendUserId,
                Map.of("referenceId", saved.getId(), "senderId", userId)
        ));
    }

    @Override
    public void respondToFriendRequest(RespondToFriendRequest request) throws BadRequestException {
        String requestId = request.getId();

        FriendShipModel friendShipModel = friendShipRepository.findById(requestId).orElseThrow(
                () -> new BadRequestException("Yêu cầu kết bạn không tồn tại")
        );

        String status = friendShipModel.getStatus();
        if (!status.equals(FriendShipStatus.SENT.getCode())) {
            throw new BadRequestException("Yêu cầu kết bạn đã được phản hồi trước đó");
        }

        if (Boolean.TRUE.equals(request.getIsAccept()))
            friendShipModel.setStatus(FriendShipStatus.ACCEPTED.getCode());
        else {
            friendShipRepository.deleteById(requestId);
            return;
        }

        friendShipRepository.save(friendShipModel);

        // Gửi thông báo realtime tới người đã gửi lời mời (requester)
        String currentUserId = AuthUtil.getCurrentUserId();
        postNotificationService.post(notificationEventFactory.build(
                NotificationEventType.ACCEPT_FRIEND,
                friendShipModel.getRequesterUserId(),
                Map.of("referenceId", requestId, "senderId", currentUserId)
        ));
    }

    @Override
    public List<UserModel> getFriends() {
        String userId = AuthUtil.getCurrentUserId();

        List<String> friendIds = friendShipRepository.getFriendIds(userId);

        return ModelMapperUtil.mapList(
                userRepository.getConversationUserInfo(friendIds),
                UserModel.class
        );
    }

    @Override
    public FriendshipStatusDTO getFriendshipStatus(String targetUserId) {
        String currentUserId = AuthUtil.getCurrentUserId();

        if (currentUserId.equals(targetUserId)) {
            return new FriendshipStatusDTO(FriendShipStatus.SELF.getCode(), null);
        }

        String lowerId = currentUserId.compareTo(targetUserId) < 0 ? currentUserId : targetUserId;
        String higherId = currentUserId.compareTo(targetUserId) < 0 ? targetUserId : currentUserId;

        FriendShipModel friendShipModel = friendShipRepository.findByUserIds(lowerId, higherId).orElse(null);

        if (friendShipModel == null) {
            return new FriendshipStatusDTO(FriendShipStatus.NONE.getCode(), null);
        }

        String status = friendShipModel.getStatus();
        String requesterUserId = friendShipModel.getRequesterUserId();

        if (status.equals(FriendShipStatus.SENT.getCode())) {
            String resolvedStatus = requesterUserId.equals(currentUserId)
                    ? FriendShipStatus.SENT.getCode()
                    : FriendShipStatus.WAITING.getCode();
            return new FriendshipStatusDTO(resolvedStatus, friendShipModel.getId());
        }

        return new FriendshipStatusDTO(status, friendShipModel.getId());
    }
}
