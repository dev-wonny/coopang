package com.coopang.user.application.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private UUID userId;
    private String email;
    private String password;
    private String userName;
    private String phoneNumber;
    private String role;
    private String slackId;

    private String zipCode;
    private String address1;
    private String address2;
    private UUID nearHubId;

    @Builder
    private UserDto(
        UUID userId
        , String email
        , String password
        , String userName
        , String phoneNumber
        , String role
        , String slackId
        , String zipCode
        , String address1
        , String address2
        , UUID nearHubId
    ) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.slackId = slackId;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.nearHubId = nearHubId;
    }

    public static UserDto of(
        UUID userId
        , String email
        , String password
        , String userName
        , String phoneNumber
        , String role
        , String slackId
        , String zipCode
        , String address1
        , String address2
        , UUID nearHubId
    ) {
        return UserDto.builder()
            .userId(userId)
            .email(email)
            .password(password)
            .userName(userName)
            .phoneNumber(phoneNumber)
            .role(role)
            .slackId(slackId)
            .zipCode(zipCode)
            .address1(address1)
            .address2(address2)
            .nearHubId(nearHubId)
            .build();
    }

    @Override
    public String toString() {
        return "UserDto{" +
            "userId=" + userId +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", userName='" + userName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", role='" + role + '\'' +
            ", slackId='" + slackId + '\'' +
            ", zipCode='" + zipCode + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            ", nearHubId=" + nearHubId +
            '}';
    }

    public void createId(UUID userId) {
        this.userId = userId;
    }
}