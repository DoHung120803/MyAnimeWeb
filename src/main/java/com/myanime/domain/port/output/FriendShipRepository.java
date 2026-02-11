package com.myanime.domain.port.output;

import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.models.FriendShipModel;

import java.util.List;
import java.util.Optional;

public interface FriendShipRepository {
    FriendShipModel save(FriendShipModel friendShipModel) throws BadRequestException;
    boolean existsByUserIds(String lowUserId, String highUserId);
    Optional<FriendShipModel> findById(String id);
    List<String> getFriendIds(String userId);
    Optional<FriendShipModel> findByUserIds(String lowUserId, String highUserId);
}
