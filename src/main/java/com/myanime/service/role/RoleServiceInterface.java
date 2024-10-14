package com.myanime.service.role;

import com.myanime.model.dto.request.role.RoleRequest;
import com.myanime.model.dto.response.RoleResponse;

import java.util.List;

public interface RoleServiceInterface {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    void delete(String role);
}
