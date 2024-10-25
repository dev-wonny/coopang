package com.coopang.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordRequestDto {
    @NotBlank(message = "New Password is required")
    private String newPassword;
}
