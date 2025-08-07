package com.myanime.infrastructure.jparepos.jpa;

import com.myanime.infrastructure.entities.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
