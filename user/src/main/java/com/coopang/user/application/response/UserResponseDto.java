package com.coopang.user.application.response;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.domain.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String email;
    private String username;
    private String phoneNumber;
    private UserRoleEnum role;
    private Boolean isBlock;

    // private 생성자, 외부에서 객체 생성을 막음
    private UserResponseDto(String email, String username, String phoneNumber, UserRoleEnum role, Boolean isBlock) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
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
                user.getEmail(),
                user.getUsername(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isBlock()
        );
    }
}
