package com.myanime.common.mapper;

import com.myanime.entity.jpa.Role;
import com.myanime.application.rest.requests.role.RoleRequest;
import com.myanime.application.rest.responses.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
