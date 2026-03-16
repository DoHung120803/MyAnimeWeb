package com.myanime.infrastructure.configurations.securities.utils;

import com.myanime.domain.models.PermissionModel;
import com.myanime.domain.models.RoleModel;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.output.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByIdWithRoles(id).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));

        return CustomUserDetails.builder()
                .id(userModel.getId())
                .username(userModel.getUsername())
                .email(userModel.getEmail())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .authorities(buildAuthorities(userModel))
                .build();
    }

    private List<GrantedAuthority> buildAuthorities(UserModel user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        List<RoleModel> roles = user.getRoles();

        if (CollectionUtils.isEmpty(roles)) {
            return authorities;
        }

        for (RoleModel role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            List<PermissionModel> permissions = role.getPermissions();

            if (!CollectionUtils.isEmpty(permissions)) {
                authorities.addAll(
                        permissions.stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                                .toList()
                );
            }
        }

        return authorities;
    }
}
