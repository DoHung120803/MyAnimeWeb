package com.myanime.domain.service.role;

import com.myanime.application.rest.requests.role.RoleRequest;
import com.myanime.application.rest.responses.RoleResponse;

import java.util.List;

public interface RoleServiceInterface {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    void delete(String role);
}
