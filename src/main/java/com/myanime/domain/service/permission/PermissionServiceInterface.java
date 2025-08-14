package com.myanime.domain.service.permission;

import com.myanime.application.rest.requests.permission.PermissionRequest;
import com.myanime.application.rest.responses.PermissionResponse;

import java.util.List;

public interface PermissionServiceInterface {
    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> getAll();
    void delete(String permission);
}
