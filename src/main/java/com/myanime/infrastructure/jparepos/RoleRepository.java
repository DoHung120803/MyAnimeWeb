package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
