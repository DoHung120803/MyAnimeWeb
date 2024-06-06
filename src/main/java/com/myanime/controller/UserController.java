package com.myanime.controller;

import com.myanime.entity.User;
import com.myanime.model.dto.request.user.UserCreationRequest;
import com.myanime.model.dto.request.user.UserUpdateRequest;
import com.myanime.model.dto.response.ApiResponse;
import com.myanime.model.dto.response.UserResponse;
import com.myanime.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("user/register")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        System.out.println(request.toString());
        return ApiResponse.<UserResponse>builder()
                .data(userService.createUser(request))
                .build();
    }

    @GetMapping("/users")
    public ApiResponse<List<User>> getUsers() {
        return ApiResponse.<List<User>>builder()
                .data(userService.getUsers())
                .build();
    }

    @GetMapping("/users/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUser(id))
                .build();
    }

    @PutMapping("users/{id}/update")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("users/{id}/delete")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

}
