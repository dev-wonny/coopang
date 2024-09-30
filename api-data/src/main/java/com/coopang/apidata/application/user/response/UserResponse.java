package com.coopang.apidata.application.user.response;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UserResponse {
    private UUID userId;
    private String email;
    private String username;
    private String phoneNumber;
    private String slackId;
    private UserRoleEnum role;
    private Boolean isBlock;
}