package com.myanime.common.mapper;

import com.myanime.infrastructure.entities.Permission;
import com.myanime.application.rest.requests.permission.PermissionRequest;
import com.myanime.application.rest.responses.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
