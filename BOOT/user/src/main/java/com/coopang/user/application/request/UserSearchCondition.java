package com.coopang.user.application.request;

import com.coopang.coredata.user.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserSearchCondition {
    private UUID userId;
    private String userName;
    private UserRoleEnum userRole;
    private String email;
    private Boolean isDeleted;

    @Builder
    private UserSearchCondition(UUID userId, String userName, UserRoleEnum userRole, String email, Boolean isDeleted) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.email = email;
        this.isDeleted = isDeleted;
    }

    public static UserSearchCondition from(UUID userId, String userName, String userRole, String email, Boolean isDeleted) {
        return UserSearchCondition.builder()
                .userId(userId)
                .userName(userName)
                .userRole(UserRoleEnum.getRoleEnum(userRole))
                .email(email)
                .isDeleted(isDeleted != null ? isDeleted : false)
                .build();
    }
}
