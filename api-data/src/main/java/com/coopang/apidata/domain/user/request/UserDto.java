package com.coopang.apidata.domain.user.request;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String username;
    private String phoneNumber;
    private String slackId;
    private String role;
}
