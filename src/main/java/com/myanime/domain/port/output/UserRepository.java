package com.myanime.domain.port.output;

import com.myanime.domain.models.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    long countByIdIn(List<String> ids);
    boolean existsById(String id);
    Optional<UserModel> findById(String id);
    List<UserModel> getConversationUserInfo(List<String> userIds);
}
