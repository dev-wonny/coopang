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
    private String userName;
    private String phoneNumber;
    private UserRoleEnum role;
    private String slackId;

    private String zipCode;
    private String address1;
    private String address2;
    private UUID nearHubId;

    private boolean isBlock;
    private boolean isDeleted;
}