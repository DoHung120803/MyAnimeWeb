package com.myanime.domain.service.user;

import com.myanime.application.rest.requests.user.UserCreationRequest;
import com.myanime.application.rest.requests.user.UserUpdateRequest;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.application.rest.responses.UserResponse;

public interface UserServiceInterface {
    UserResponse createUser(UserCreationRequest request);
    PageResponse<UserResponse> getUsers(int page, int size);
    UserResponse getUser(String id);
    UserResponse getMyInfo();
    UserResponse updateUser(String id, UserUpdateRequest request);
    void deleteUser(String id);
}
