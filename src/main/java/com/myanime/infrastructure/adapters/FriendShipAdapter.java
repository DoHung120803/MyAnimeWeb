package com.myanime.infrastructure.adapters;

import com.myanime.common.exceptions.BadRequestException;
import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.FriendShipModel;
import com.myanime.domain.port.output.FriendShipRepository;
import com.myanime.infrastructure.entities.Friendship;
import com.myanime.infrastructure.jparepos.FriendshipJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FriendShipAdapter implements FriendShipRepository {

    private final FriendshipJpaRepository friendshipJpaRepository;

    @Override
    public FriendShipModel save(FriendShipModel friendShipModel) throws BadRequestException {
        if (friendShipModel == null) {
            return null;
        }

        Friendship saved = friendshipJpaRepository.save(ModelMapperUtil.mapper(friendShipModel, Friendship.class));
        return ModelMapperUtil.mapper(saved, FriendShipModel.class);
    }

    @Override
    public boolean existsByUserIds(String lowUserId, String highUserId) {
        if (!StringUtils.hasText(lowUserId) || !StringUtils.hasText(highUserId)) {
            return false;
        }

        return friendshipJpaRepository.existsByLowUserIdAndHighUserId(lowUserId, highUserId);
    }

    @Override
    public Optional<FriendShipModel> findById(String id) {
        if (!StringUtils.hasText(id)) {
            return Optional.empty();
        }

        Optional<Friendship> friendshipOpt = friendshipJpaRepository.findById(id);

        return friendshipOpt.map(entity -> ModelMapperUtil.mapper(entity, FriendShipModel.class));
    }

    @Override
    public List<String> getFriendIds(String userId) {
        if (!StringUtils.hasText(userId)) {
            return List.of();
        }

        return friendshipJpaRepository.getFriendIds(userId);
    }

    @Override
    public Optional<FriendShipModel> findByUserIds(String lowUserId, String highUserId) {
        if (!StringUtils.hasText(lowUserId) || !StringUtils.hasText(highUserId)) {
            return Optional.empty();
        }

        return friendshipJpaRepository.findByLowUserIdAndHighUserId(lowUserId, highUserId)
                .map(entity -> ModelMapperUtil.mapper(entity, FriendShipModel.class));
    }
}
