package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.User;
import com.myanime.infrastructure.projections.ConversationUserInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    long countByIdIn(List<String> ids);

    @Query("SELECT u.id as id, u.avtUrl as avtUrl, u.firstName as firstName, u.lastName as lastName FROM User u WHERE u.id IN :userIds")
    List<ConversationUserInfoProjection> getConversationUserInfoByIds(List<String> userIds);
}
