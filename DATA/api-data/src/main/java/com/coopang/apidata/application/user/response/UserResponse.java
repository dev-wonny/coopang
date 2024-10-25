package com.coopang.apidata.application.user.response;

import com.coopang.apidata.application.address.Address;
import com.coopang.coredata.user.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID userId;
    private String email;
    private String userName;
    private String phoneNumber;
    private UserRoleEnum role;
    private String slackId;

    private Address address;
    private UUID nearHubId;

    private boolean isBlock;
    private boolean isDeleted;
}