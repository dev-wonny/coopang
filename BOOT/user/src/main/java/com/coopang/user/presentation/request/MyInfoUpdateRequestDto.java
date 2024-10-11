package com.coopang.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyInfoUpdateRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣 ]{2,10}$", message = "Username must contain only letters, Korean characters, and optional spaces")
    private String userName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String slackId;
}