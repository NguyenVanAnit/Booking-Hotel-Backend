package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.User;
import com.nguyenvanancodeweb.lakesidehotel.request.RegisterRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.UserResponse;

import java.util.List;

public interface IUserService {
    void registerUser(RegisterRequest requestUser);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
    User getUserByUserId(Long userId);
    List<User> getAllUsers();
    UserResponse getUserResponse(String email);
}
