package com.coopang.hub.application.service;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.apidata.application.user.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public UserResponse getUserInfo(UUID userId) {
        return userClient.getUserInfo(userId);
    }

    public UserRoleEnum getRole(UUID userId) {
        return userClient.getUserInfo(userId).getRole();
    }
}
