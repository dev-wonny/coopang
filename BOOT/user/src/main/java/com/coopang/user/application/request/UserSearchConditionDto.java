package com.coopang.user.application.request;

import com.coopang.coredata.user.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
public class UserSearchConditionDto {
    private UUID userId;
    private String userName;
    private UserRoleEnum userRole;
    private String email;
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
            .userRole(!ObjectUtils.isEmpty(userRole) ? UserRoleEnum.getRoleEnum(userRole) : null)
            .email(email)
            .isDeleted(!ObjectUtils.isEmpty(isDeleted) && isDeleted)
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            UserSearchConditionDto that = (UserSearchConditionDto) o;
            return
                Objects.equals(this.userId, that.userId)
                    && Objects.equals(this.userName, that.userName)
                    && this.userRole == that.userRole
                    && Objects.equals(this.email, that.email)
                    && Objects.equals(this.isDeleted, that.isDeleted)
                ;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userId, this.userName, this.userRole, this.email, this.isDeleted);
    }

    @Override
    public String toString() {
        String var10000 = String.valueOf(this.userId);
        return "UserSearchConditionDto(userId=" + var10000 + ", userName=" + this.userName + ", userRole=" + this.userRole + ", email=" + this.email + ", isDeleted=" + this.isDeleted + ")";
    }
}