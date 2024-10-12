package com.coopang.apicommunication.feignclient.user;

import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.user.request.UserSearchConditionRequest;
import com.coopang.apidata.application.user.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user", configuration = FeignConfig.class)
public interface UserClient {
    @GetMapping("/users/v1/user/{userId}")
    UserResponse getUserInfo(@PathVariable("userId") UUID userId);

    @PostMapping("/users/v1/user/list")
    List<UserResponse> getUserList(@RequestBody UserSearchConditionRequest req);
}