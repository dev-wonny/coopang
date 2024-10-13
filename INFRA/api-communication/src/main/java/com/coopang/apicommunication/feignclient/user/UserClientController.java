package com.coopang.apicommunication.feignclient.user;

import com.coopang.apidata.application.user.request.UserSearchConditionRequest;
import com.coopang.apidata.application.user.response.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "UserClientController API", description = "UserClientController API")
@Slf4j(topic = "UserClientController")
@RestController
@RequestMapping("/feignClient/v1/user")
public class UserClientController {
    private final UserClientService userClientService;

    public UserClientController(UserClientService userClientService) {
        this.userClientService = userClientService;
    }

    @GetMapping("/{userId}")
    public UserResponse getUserInfo(@PathVariable("userId") UUID userId) {
        return userClientService.getUserInfo(userId);
    }

    @PostMapping("/list")
    public List<UserResponse> getUserList(@Valid @RequestBody UserSearchConditionRequest req) {
        return userClientService.getUserList(req);
    }
}