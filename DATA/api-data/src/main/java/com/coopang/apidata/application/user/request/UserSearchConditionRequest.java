package com.coopang.apidata.application.user.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserSearchConditionRequest {
    private UUID userId;
    private String userName;
    private String userRole;
    @Email(message = "Email should be valid")
    private String email;
    private Boolean isDeleted;
}
