package com.coopang.apiconfig.security;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserMetadata {
    private String userId;
    private String userEmail;
    private String token;

    @Builder
    private UserMetadata(String userId, String userEmail, String token) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.token = token;
    }

    public static UserMetadata of(String userId, String userEmail, String token) {
        return UserMetadata.builder()
            .userId(userId)
            .userEmail(userEmail)
            .token(token)
            .build();
    }
}
