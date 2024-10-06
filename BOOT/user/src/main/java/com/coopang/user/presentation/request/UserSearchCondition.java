package com.coopang.user.presentation.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchCondition {
    private String userName;
    private String userRole;
    private String email;
}
