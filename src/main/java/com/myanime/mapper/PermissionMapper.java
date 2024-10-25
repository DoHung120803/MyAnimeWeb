package com.myanime.mapper;

import com.myanime.entity.jpa.Permission;
import com.myanime.model.dto.request.permission.PermissionRequest;
import com.myanime.model.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
