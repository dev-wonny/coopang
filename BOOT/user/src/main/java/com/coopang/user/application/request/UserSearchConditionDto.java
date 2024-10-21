package com.coopang.user.application.request;

import com.coopang.coredata.user.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
public class UserSearchConditionDto {
    private UUID userId;
    private String userName;// StartsWith search functionality
    private UserRoleEnum userRole;
    private String email;// equal search functionality
    private boolean isDeleted;

    @Builder
    private UserSearchConditionDto(UUID userId, String userName, UserRoleEnum userRole, String email, boolean isDeleted) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.email = email;
        this.isDeleted = isDeleted;
    }

    /**
     * 기본값을 가진 빈 객체를 반환하는 정적 메서드
     *
     * @return
     */
    public static UserSearchConditionDto empty() {
        return UserSearchConditionDto.builder()
            .userId(null)
            .userName(null)
            .userRole(null)
            .email(null)
            .isDeleted(false)
            .build();
    }

    public static UserSearchConditionDto from(UUID userId, String userName, String userRole, String email, boolean isDeleted) {
        return UserSearchConditionDto.builder()
            .userId(userId)
            .userName(userName)
            .userRole(!StringUtils.isBlank(userRole) ? UserRoleEnum.getRoleEnum(userRole) : null)
            .email(email)
            .isDeleted(!ObjectUtils.isEmpty(isDeleted) && isDeleted)
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSearchConditionDto that = (UserSearchConditionDto) o;
        return isDeleted == that.isDeleted && Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName) && userRole == that.userRole &&
            Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userRole, email, isDeleted);
    }

    @Override
    public String toString() {
        return "UserSearchConditionDto{" +
            "userId=" + userId +
            ", userName='" + userName + '\'' +
            ", userRole=" + userRole +
            ", email='" + email + '\'' +
            ", isDeleted=" + isDeleted +
            '}';
    }
}