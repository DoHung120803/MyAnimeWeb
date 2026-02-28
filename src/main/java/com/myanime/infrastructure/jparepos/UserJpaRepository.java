package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.User;
import com.myanime.infrastructure.projections.ConversationUserInfoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    long countByIdIn(List<String> ids);

    @Query("SELECT u.id as id, u.avtUrl as avtUrl, u.firstName as firstName, u.lastName as lastName FROM User u WHERE u.id IN :userIds")
    List<ConversationUserInfoProjection> getConversationUserInfoByIds(List<String> userIds);

    @Query("SELECT u FROM User u WHERE :minId IS NULL OR u.id > :minId ORDER BY u.id ASC LIMIT :limit")
    List<User> findByMinIdAndLimit(@Param("minId") String minId, @Param("limit") Integer limit);

    @Query("""
                SELECT u.id as id, u.avtUrl as avtUrl, u.firstName as firstName, u.lastName as lastName, u.username as username
                FROM User u
                WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName))
                      LIKE LOWER(CONCAT('%', :keyword, '%'))
                ORDER BY u.createdAt DESC
            """)
    Page<ConversationUserInfoProjection> searchUser(@Param("keyword") String keyword, Pageable pageable);

}
