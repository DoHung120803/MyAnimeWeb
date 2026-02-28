package com.myanime.domain.port.input;

import com.myanime.application.rest.requests.user.UserCreationRequest;
import com.myanime.application.rest.requests.user.UserUpdateRequest;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.application.rest.responses.UserResponse;
import com.myanime.domain.models.UserModel;
import org.springframework.data.domain.Pageable;

public interface UserUC {
    UserResponse createUser(UserCreationRequest request);
    PageResponse<UserResponse> getUsers(int page, int size);
    UserResponse getUser(String id);
    UserResponse getUserByUsername(String username);
    UserResponse getMyInfo();
    UserResponse updateUser(String id, UserUpdateRequest request);
    void deleteUser(String id);
    PageResponse<UserModel> searchUsers(String keyword, Pageable pageable);
}
