package com.coopang.user.application.response;

import com.coopang.apidata.domain.user.enums.UserRoleEnum;
import com.coopang.user.domain.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private UUID userId;
    private String email;
    private String username;
    private String phoneNumber;
    private String slackId;
    private UserRoleEnum role;
    private Boolean isBlock;

    // private 생성자, 외부에서 객체 생성을 막음


    private UserResponseDto(UUID userId, String email, String username, String phoneNumber, String slackId, UserRoleEnum role, Boolean isBlock) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.slackId = slackId;
        this.role = role;
        this.isBlock = isBlock;
    }

    /**
     * 오로지 하나의 객체만 반환하도록 하여 객체를 재사용해 메모리를 아끼도록 유도
     *
     * @param user
     * @return
     */
    public static UserResponseDto fromUser(UserEntity user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getPhoneNumber(),
                user.getSlackId(),
                user.getRole(),
                user.isBlock()
        );
    }
}
