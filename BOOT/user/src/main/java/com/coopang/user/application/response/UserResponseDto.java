package com.coopang.user.application.response;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.user.domain.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private UUID userId;
    private String email;
    private String userName;
    private String phoneNumber;
    private UserRoleEnum role;
    private String slackId;

    private String zipCode;
    private String address1;
    private String address2;
    private UUID nearHubId;

    private boolean isBlock;
    private boolean isDeleted;

    // private 생성자, 외부에서 객체 생성을 막음
    private UserResponseDto(
            UUID userId,
            String email,
            String userName,
            String phoneNumber,
            UserRoleEnum role,
            String slackId,
            String zipCode,
            String address1,
            String address2,
            UUID nearHubId,
            boolean isBlock,
            boolean isDeleted
    ) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.slackId = slackId;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.nearHubId = nearHubId;
        this.isBlock = isBlock;
        this.isDeleted = isDeleted;

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
                user.getUserName(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getSlackId(),
                user.getAddressEntity().getZipCode(),
                user.getAddressEntity().getAddress1(),
                user.getAddressEntity().getAddress2(),
                user.getNearHubId(),
                user.isBlock(),
                user.isDeleted()
        );
    }
}
