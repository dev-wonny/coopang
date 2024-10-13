package com.coopang.user.presentation.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserSearchConditionRequestDto {
    private UUID userId;
    private String userName;
    private String userRole;
    @Email(message = "Email should be valid")
    private String email;
    private Boolean isDeleted;
}
