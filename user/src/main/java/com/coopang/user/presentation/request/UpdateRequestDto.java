package com.coopang.user.presentation.request;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequestDto {
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z가-힣 ]{2,10}$", message = "Username must contain only letters, Korean characters, and optional spaces")
    private String username;
    private String phoneNumber;
    private String slackId;

    @NotNull(message = "Role is required.")
    private UserRoleEnum role;

    private String adminToken = "";
}