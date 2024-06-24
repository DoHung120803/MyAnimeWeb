package com.myanime.mapper;

import com.myanime.entity.Permission;
import com.myanime.entity.Role;
import com.myanime.model.dto.request.permission.PermissionRequest;
import com.myanime.model.dto.request.role.RoleRequest;
import com.myanime.model.dto.response.PermissionResponse;
import com.myanime.model.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
