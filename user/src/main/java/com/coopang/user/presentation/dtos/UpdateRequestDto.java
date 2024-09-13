package com.coopang.user.presentation.dtos;

import com.coopang.user.application.enums.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequestDto {
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_-]{4,10}$", message = "Username must contain only lowercase letters and numbers")
    private String username;
    private String phoneNumber;
    private String slackId;

    @NotNull(message = "Role is required.")
    private UserRoleEnum role;

    private String adminToken = "";
}