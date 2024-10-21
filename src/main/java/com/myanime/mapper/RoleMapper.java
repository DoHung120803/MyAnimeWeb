package com.myanime.mapper;

import com.myanime.entity.jpa.Role;
import com.myanime.model.dto.request.role.RoleRequest;
import com.myanime.model.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
