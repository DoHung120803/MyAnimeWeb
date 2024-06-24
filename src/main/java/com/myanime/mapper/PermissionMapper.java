package com.myanime.mapper;

import com.myanime.entity.Permission;
import com.myanime.entity.User;
import com.myanime.model.dto.request.permission.PermissionRequest;
import com.myanime.model.dto.request.user.UserCreationRequest;
import com.myanime.model.dto.request.user.UserUpdateRequest;
import com.myanime.model.dto.response.PermissionResponse;
import com.myanime.model.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
