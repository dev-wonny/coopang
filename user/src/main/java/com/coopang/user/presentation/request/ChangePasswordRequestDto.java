package com.coopang.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class ChangePasswordRequestDto {

    @NotNull(message = "UserId는 필수 입력입니다.")
    private UUID userId;

    @NotBlank(message = "현재 비밀번호는 필수 입력입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수 입력입니다.")
    private String newPassword;
}