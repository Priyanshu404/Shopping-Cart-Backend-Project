package com.mycodework.yourshops.service.user;

import com.mycodework.yourshops.dto.UserDto;
import com.mycodework.yourshops.model.User;
import com.mycodework.yourshops.request.CreateUserRequest;
import com.mycodework.yourshops.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
