package com.myanime.domain.port.output;

import com.myanime.domain.models.UserModel;

import java.util.List;

public interface UserRepository {
    long countByIdIn(List<String> ids);

    List<UserModel> getConversationUserInfo(List<String> userIds);
}
