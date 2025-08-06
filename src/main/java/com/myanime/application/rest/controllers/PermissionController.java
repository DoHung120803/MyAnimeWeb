package com.myanime.application.rest.controllers;

import com.myanime.application.rest.requests.permission.PermissionRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.application.rest.responses.PermissionResponse;
import com.myanime.domain.service.permission.PermissionServiceInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/permissions")
public class PermissionController {
    PermissionServiceInterface permissionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.create(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAll())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{permission}")
    public ApiResponse<Void> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        return ApiResponse.<Void>builder()
                .message("Delete permission successfully!")
                .build();
    }
}
