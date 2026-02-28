package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipJpaRepository extends JpaRepository<Friendship, String> {
    boolean existsByLowUserIdAndHighUserId(String lowUserId, String highUserId);

    @Query(value = """
            SELECT
                CASE
                    WHEN low_user_id = :userId THEN high_user_id
                    ELSE low_user_id
                    END AS friend_user_id
            FROM friendships
            WHERE status = 'ACCEPTED'
              AND (low_user_id = :userId OR high_user_id = :userId);
            """, nativeQuery = true)
    List<String> getFriendIds(String userId);

    @Query(value = """
            SELECT * FROM friendships
            WHERE low_user_id = :lowUserId AND high_user_id = :highUserId
            ORDER BY updated_at DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<Friendship> findByLowUserIdAndHighUserId(String lowUserId, String highUserId);

}
