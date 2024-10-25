package com.coopang.product.application.service.feignclient;

import com.coopang.apicommunication.feignclient.user.UserClientService;
import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFeignClientService {

    private final UserClientService userClientService;
    private final FeignConfig feignConfig;

    //슬랙 아이디 조회
    public UserResponse getUserById(UUID userId) {
        try {
            feignConfig.changeHeaderRoleToServer();
            return userClientService.getUserInfo(userId);
        } finally {
            feignConfig.resetRole();
        }
    }
}
