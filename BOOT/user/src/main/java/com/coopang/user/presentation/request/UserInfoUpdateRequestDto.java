package com.coopang.user.presentation.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoUpdateRequestDto {
    @Pattern(regexp = "^[a-zA-Z가-힣 ]{2,10}$", message = "Username must contain only letters, Korean characters, and optional spaces")
    private String userName;
    private String phoneNumber;
    private String slackId;
    private String role;
}