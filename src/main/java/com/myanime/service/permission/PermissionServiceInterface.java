package com.myanime.service.permission;

import com.myanime.model.dto.request.permission.PermissionRequest;
import com.myanime.model.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionServiceInterface {
    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> getAll();
    void delete(String permission);
}
