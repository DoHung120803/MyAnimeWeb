package com.myanime.application.rest.controllers;

import com.myanime.application.rest.requests.role.RoleRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.application.rest.responses.RoleResponse;
import com.myanime.domain.service.role.RoleServiceInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/roles")
public class RoleController {
    RoleServiceInterface roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.create(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .data(roleService.getAll())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{role}")
    public ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder()
                .message("Delete role successfully!")
                .build();
    }
}
