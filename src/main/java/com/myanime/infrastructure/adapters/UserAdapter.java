package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.output.UserRepository;
import com.myanime.infrastructure.jparepos.UserJpaRepository;
import com.myanime.infrastructure.projections.ConversationUserInfoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public long countByIdIn(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) return 0;

        return userJpaRepository.countByIdIn(ids);
    }

    @Override
    public boolean existsById(String id) {
        if (!StringUtils.hasText(id)) return false;

        return userJpaRepository.existsById(id);
    }

    @Override
    public Optional<UserModel> findById(String id) {
        if (!StringUtils.hasText(id)) return Optional.empty();

        return userJpaRepository.findById(id)
                .map(user -> ModelMapperUtil.mapper(user, UserModel.class));
    }

    @Override
    public List<UserModel> getConversationUserInfo(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return List.of();
        }

        List<ConversationUserInfoProjection> projections = userJpaRepository.getConversationUserInfoByIds(userIds);

        return ModelMapperUtil.mapList(projections, UserModel.class);
    }
}
