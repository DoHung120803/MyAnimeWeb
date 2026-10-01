package com.myanime.domain.port.output;

import com.myanime.domain.models.UserDetailModel;
import com.myanime.domain.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends SyncableRepository<UserModel> {
    UserModel save(UserModel userModel);
    long countByIdIn(List<String> ids);
    boolean existsById(String id);
    boolean existsByUsername(String username);
    Optional<UserModel> findById(String id);
    List<UserModel> findAllByIds(List<String> ids);
    List<UserModel> getConversationUserInfo(List<String> userIds);
    List<UserModel> findByMinIdAndLimit(String minId, Integer limit);
    Page<UserModel> search(String keyword, Pageable pageable);
    Optional<UserModel> findByIdWithRoles(String id);
    Optional<UserDetailModel> findByUsernameWithRoles(String username);
    Optional<UserModel> findByEmail(String email);
}
