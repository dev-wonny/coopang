package com.coopang.gateway.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HeaderResponseDto {
    private String token;
    private String userEmail;
    private String userId;
    private String userRole;

    @Builder
    private HeaderResponseDto(String token, String userEmail, String userId, String userRole) {
        this.token = token;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userRole = userRole;
    }

    public static HeaderResponseDto of(String token, String userEmail, String userId, String role) {
        return HeaderResponseDto.builder()
                .token(token)
                .userId(userId)
                .userEmail(userEmail)
                .userRole(role)
                .build();
    }
}
