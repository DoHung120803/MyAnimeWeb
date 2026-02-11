package com.myanime.domain.service.friends;

import com.myanime.application.rest.requests.friends.AddFriendRequest;
import com.myanime.application.rest.requests.friends.RespondToFriendRequest;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.common.utils.AuthUtil;
import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.dtos.notifies.NotificationDTO;
import com.myanime.domain.enums.FriendShipStatus;
import com.myanime.domain.enums.NotificationEventType;
import com.myanime.domain.models.FriendShipModel;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.input.FriendShipUC;
import com.myanime.domain.port.output.FriendShipRepository;
import com.myanime.domain.port.output.UserRepository;
import com.myanime.domain.service.notifies.PostNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class FriendShipService implements FriendShipUC {

    private final UserRepository userRepository;
    private final FriendShipRepository friendShipRepository;
    private final PostNotificationService postNotificationService;
    private final ExecutorService executorService;


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

        FriendShipModel saved = friendShipRepository.save(friendShipModel);

        // Gửi thông báo realtime tới người nhận lời mời kết bạn
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setReceiver(friendUserId);
        notificationDTO.setTitle("Lời mời kết bạn");
        notificationDTO.setMessage("đã gửi cho bạn lời mời kết bạn");
        notificationDTO.setMetaData(Map.of(
                "type", NotificationEventType.FRIEND_REQUEST.getCode(),
                "referenceId", saved.getId(),
                "senderId", userId
        ));
        postNotificationService.post(notificationDTO);
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

        friendShipModel.setStatus(Boolean.TRUE.equals(request.getIsAccept()) ? FriendShipStatus.ACCEPTED.getCode() : FriendShipStatus.REJECTED.getCode());

        friendShipRepository.save(friendShipModel);
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
    public String getFriendshipStatus(String targetUserId) {
        String currentUserId = AuthUtil.getCurrentUserId();

        if (currentUserId.equals(targetUserId)) {
            return "SELF";
        }

        String lowerId = currentUserId.compareTo(targetUserId) < 0 ? currentUserId : targetUserId;
        String higherId = currentUserId.compareTo(targetUserId) < 0 ? targetUserId : currentUserId;

        return friendShipRepository.findByUserIds(lowerId, higherId)
                .map(FriendShipModel::getStatus)
                .orElse("NONE");
    }
}
