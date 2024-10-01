package com.coopang.apicommunication.feignClient.user;

import com.coopang.apiconfig.feignClient.FeignConfig;
import com.coopang.apidata.application.user.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user", configuration = FeignConfig.class)
public interface UserClient {
    @GetMapping("/users/v1/user/{userId}")
    UserResponse getUserInfo(@PathVariable("userId") UUID userId);
}