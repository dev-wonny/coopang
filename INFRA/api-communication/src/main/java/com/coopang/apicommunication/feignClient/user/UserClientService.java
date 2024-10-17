package com.coopang.apicommunication.feignclient.user;

import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.apidata.application.user.request.UserSearchConditionRequest;
import com.coopang.apidata.application.user.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<UserResponse> getUserList(UserSearchConditionRequest req) {
        return userClient.getUserList(req);
    }
}