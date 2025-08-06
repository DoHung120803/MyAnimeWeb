package com.myanime.domain.service.role;

import com.myanime.entity.jpa.Role;
import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.common.mapper.RoleMapper;
import com.myanime.application.rest.requests.role.RoleRequest;
import com.myanime.application.rest.responses.RoleResponse;
import com.myanime.repository.jpa.PermissionRepository;
import com.myanime.repository.jpa.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements RoleServiceInterface {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        if (permissions.isEmpty())
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);

        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}

