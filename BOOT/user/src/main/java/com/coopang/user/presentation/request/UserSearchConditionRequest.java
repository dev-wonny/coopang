package com.coopang.user.presentation.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserSearchConditionRequest {
    private UUID userId;
    private String userName;
    private String userRole;
    private String email;
    private Boolean isDeleted;
}
