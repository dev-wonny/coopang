package com.coopang.user.presentation.request;

import lombok.Data;

@Data
public class UserSearchCondition {
    private String userName;
    private String userRole;
    private String email;
}
