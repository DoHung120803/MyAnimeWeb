package com.myanime.repository.jpa;

import com.myanime.entity.jpa.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
