package com.myanime.domain.service.user;

import com.myanime.common.utils.JsonUtil;
import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.port.input.UserUC;
import com.myanime.infrastructure.entities.jpa.Role;
import com.myanime.infrastructure.entities.jpa.User;
import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.common.mapper.UserMapper;
import com.myanime.application.rest.requests.user.UserCreationRequest;
import com.myanime.application.rest.requests.user.UserUpdateRequest;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.application.rest.responses.UserResponse;
import com.myanime.infrastructure.jparepos.jpa.RoleRepository;
import com.myanime.infrastructure.jparepos.jpa.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements UserUC {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.registration-notify}")
    @NonFinal
    String registrationTopic;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(com.myanime.domain.enums.Role.USER.name()).ifPresent(roles::add);

        user.setRoles(roles);

//        UserResponse userResponse = ModelMapperUtil.mapper(userRepository.save(user), UserResponse.class);
        kafkaTemplate.send(registrationTopic, JsonUtil.toString(user));

        return  null;
    }

    //  we can use  @PreAuthorize("hasAnyAuthority('permission')")
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1, size, Sort.by(Sort.Direction.ASC, "username")
        );
        Page<User> users = userRepository.findAll(pageable);

        return PageResponse.<UserResponse>builder()
                .content(users.stream()
                        .map(userMapper::toUserResponse)
                        .toList())
                .currentPage(page)
                .pageSize(size)
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
    }

    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        log.info("in postauthorize method");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

//        var roles = roleRepository.findAllById(request.getRoles());
//        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
