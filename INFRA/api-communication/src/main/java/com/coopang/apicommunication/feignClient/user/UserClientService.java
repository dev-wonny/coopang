package com.coopang.apicommunication.feignClient.user;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.apidata.application.user.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserClientService {
    private final UserClient userClient;

    public UserClientService(UserClient userClient) {
        this.userClient = userClient;
    }

    public UserResponse getUserInfo(UUID userId) {
        return userClient.getUserInfo(userId);
    }

    public UserRoleEnum getRole(UUID userId) {
        return userClient.getUserInfo(userId).getRole();
    }
}
