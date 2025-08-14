package com.myanime.infrastructure.jparepos.jpa;

import com.myanime.infrastructure.entities.jpa.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
