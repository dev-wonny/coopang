package com.coopang.user.application.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private String email;
    private String password;
    private String userName;
    private String phoneNumber;
    private String role;
    private String slackId;

    private String zipCode;
    private String address1;
    private String address2;
    private UUID nearHubId;
}