package com.myanime.service.user;

import com.myanime.model.dto.request.user.UserCreationRequest;
import com.myanime.model.dto.request.user.UserUpdateRequest;
import com.myanime.model.dto.response.PageResponse;
import com.myanime.model.dto.response.UserResponse;

public interface UserServiceInterface {
    UserResponse createUser(UserCreationRequest request);
    PageResponse<UserResponse> getUsers(int page, int size);
    UserResponse getUser(String id);
    UserResponse getMyInfo();
    UserResponse updateUser(String id, UserUpdateRequest request);
    void deleteUser(String id);
}
