package com.coopang.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChangePasswordRequestDto {

    @NotBlank(message = "Current Password is required")
    private String currentPassword;

    @NotBlank(message = "New Password is required")
    private String newPassword;
}